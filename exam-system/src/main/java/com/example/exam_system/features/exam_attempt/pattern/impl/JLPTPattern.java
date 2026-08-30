package com.example.exam_system.features.exam_attempt.pattern.impl;
import com.example.exam_system.common.rules.JLPTRules;
import com.example.exam_system.features.exam_attempt.dto.response.ScoreFailResponse;
import com.example.exam_system.common.enums.ATTEMPTSTATUS;
import com.example.exam_system.common.enums.EXAMTYPE;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.exam.dto.response.ExamFieldResponse;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam.entity.ExamPart;
import com.example.exam_system.features.exam.repository.projection.QuestionProjection;
import com.example.exam_system.features.exam.service.ExamPartService;
import com.example.exam_system.features.exam.service.ExamService;
import com.example.exam_system.features.exam.service.OptionService;
import com.example.exam_system.features.exam.service.QuestionService;
import com.example.exam_system.features.exam_attempt.dto.request.AnswerRequest;
import com.example.exam_system.features.exam_attempt.dto.response.GradingResultResponse;
import com.example.exam_system.features.exam_attempt.dto.response.PassFailResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.exam_attempt.entity.StudentAnswer;
import com.example.exam_system.features.exam_attempt.pattern.grading.GradingStrategy;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import com.example.exam_system.features.exam_attempt.repository.ExamAttemptRepository;
import com.example.exam_system.features.exam_attempt.repository.StudentAnswerRepository;
import com.example.exam_system.features.exam_attempt.repository.projection.AnswerProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JLPTPattern implements ExamStrategy {

    private final GradingStrategy<PassFailResponse> gradingStrategy;
    // @RequiredArgsConstructor không tự động copy @Qualifier từ Field vào Tham số Constructor
    private final ExamAttemptRepository attemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamService examService;
    private final ExamPartService partService;
    private final QuestionService questionService;
    private final OptionService optionService;


    @Override
    public String examType() {
        return EXAMTYPE.JLPT.name();
    }

    @Override
    public ExamFieldResponse getQuestion(ExamAttempt attempt) {
        Long currentPart = attempt.getCurrentPart().getId();
        return examService.getQuestionTest(attempt.getExam().getPublicId(), Arrays.asList(currentPart));
    }

    @Override
    public ExamAttempt startExam(User user, Exam exam) {
        // Tư tưởng: Khởi tạo bài thi -> Hiển thị Câu hỏi Part đầu tiên bài thi đó
        // Nộp bài(Kiểm tra Part cuối cùng chưa ? chấm điểm : hiển thị câu hỏi Part tiếp
        // theo)
        List<ExamPart> getAllPart = partService.getAllPartExam(exam.getId());
        ExamAttempt attempt = ExamAttempt.builder()
                .startTime(LocalDateTime.now())
                .submitTime(null)
                .status(ATTEMPTSTATUS.STARTED.name())
                .currentPart(getAllPart.get(0))
                .correctAnswersCount(0L)
                .totalScore(0.0)
                .isPassed(true)
                .user(user)
                .exam(exam)
                .build();
        attemptRepository.save(attempt);
        return attempt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSaveAnswer(ExamAttempt examAttempt, List<AnswerRequest> studentAnswer) {
        // Tư tưởng: Cứ sau mỗi 30s thì FE sẽ gửi câu hỏi người dùng chọn xuống để lưu
        // B1: Kiểm tra câu hỏi phải nằm trong Part hiện tại ? upSert : báo lỗi cheating
        // B2: Lấy danh sách tất cả câu hỏi mà Student chọn trong part đó
        // B3: Nếu câu hỏi đã lưu trong StudentResponse ? Update : Create

        List<Long> questionId = studentAnswer.stream().map(value -> value.getQuestionId())
                .collect(Collectors.toList());
        Long currentPart = examAttempt.getCurrentPart().getId();
        Long countQuestionInvalid = questionService.countQuestionInvalid(currentPart, questionId);
        if (countQuestionInvalid > 0) {
            throw new AppException(ErrorCode.QUESTION_NOT_VALID);
        }

        // Lấy danh sách tất cả câu hỏi mà Student chọn trong part đó
        List<StudentAnswer> getQuestion = studentAnswerRepository.getAllQuestionAnswer(examAttempt.getId(), questionId);
        // Mapping: questionId, StudentAnswer
        Map<Long, StudentAnswer> mappingQuestion = getQuestion.stream()
                .collect(Collectors.toMap(
                        request -> request.getQuestion().getId(),
                        request -> request));

        List<StudentAnswer> upSert = new ArrayList<>();
        for (AnswerRequest request : studentAnswer) {
            Long quesId = request.getQuestionId();
            Long optionId = request.getOptionId();
            StudentAnswer stAnswer = mappingQuestion.get(quesId);
            if (Objects.isNull(stAnswer)) {
                // Tạo mới
                stAnswer = StudentAnswer.builder()
                        .examAttempt(examAttempt)
                        .updatedAt(LocalDateTime.now())
                        .question(questionService.getReferenceQuestion(quesId))
                        .selectedOption(optionService.getReferenceOption(optionId))
                        .build();
            } else {
                // Cập nhật Option
                stAnswer.setSelectedOption(optionService.getReferenceOption(optionId));
            }
            upSert.add(stAnswer);
        }
        studentAnswerRepository.saveAll(upSert);
    }

    @Override
    public ExamAttempt submitExam(ExamAttempt attempt) {
        String currentPartName = attempt.getCurrentPart().getName();
        // Tư tưởng: Kiểm tra đây đã là part cuối cùng chưa : updateStatus ? cập nhật
        List<ExamPart> getAllPart = partService.getAllPartExam(attempt.getExam().getId());
        List<Long> partIdList = getAllPart.stream()
                .map(value -> value.getId()).collect(Collectors.toList());
        // Lấy giá trị tại chỉ số cuối cùng của phần tử trong List
        Long lastPartId = Long.valueOf(partIdList.get(partIdList.size() - 1));

        // Tính điểm từng Part
        GradingResultResponse calculate = calculateScore(attempt);
        Long totalCountAnswer = attempt.getCorrectAnswersCount() + calculate.getCountQuesCorrect();
        Double totalScore = attempt.getTotalScore() + calculate.getScore();

        boolean lastPart = nextCurrentPart(attempt, partIdList, lastPartId);

        // Quy đổi điểm, kiểm tra điểm liệt
        GradingResultResponse gradingResult = convertScore(
                currentPartName,
                attempt.getExam().getLevel(),
                calculate.getScore(), totalScore,
                lastPart, attempt.getIsPassed());

        // chỉ update khi chưa bị liệt, nếu liệt rồi không ghi đè update mới
        if (attempt.getIsPassed()) {
            attempt.setIsPassed(gradingResult.isPassed());
            attempt.setMessage(gradingResult.getMessage());
        }

        attempt.setCorrectAnswersCount(totalCountAnswer);
        attempt.setTotalScore(totalScore);
        attemptRepository.save(attempt);
        return attempt;
    }

    @Override
    public GradingResultResponse calculateScore(ExamAttempt attempt) {
        // Tư tưởng: Cập nhật trạng thái bài thi tránh Double Submit
        // Lấy toàn bộ câu hỏi trong bài kiểm tra theo examId
        // Lấy toàn bộ câu hỏi mà người dùng chọn trong bài kiểm tra đó =>
        // Map<questionId, Option>
        // So sánh đối chiếu 2 option trong Map theo questionId
        Long currentPartId = attempt.getCurrentPart().getId();
        List<QuestionProjection> questionCorrect = optionService.getQuestionByPartId(currentPartId);

        List<AnswerProjection> questionAnswer = studentAnswerRepository.getAllQuestionJLPT(attempt.getId(),
                currentPartId);
        Map<Long, Long> mapQuesAnswer = questionAnswer.stream()
                .collect(Collectors.toMap(
                        value -> value.getQuestionId(),
                        value -> value.getOptionId()));

        long countQuesCorrect = 0;
        double score = 0.0;
        for (QuestionProjection request : questionCorrect) {
            Long quesCorrectId = request.getQuestionId();
            Long optCorrectId = request.getOptionId();
            Long optAnswer = mapQuesAnswer.get(quesCorrectId);
            if (!Objects.isNull(optAnswer) && Objects.equals(optCorrectId, optAnswer)) {
                score += request.getScoreWeight();
                countQuesCorrect++;
            }
        }

        return GradingResultResponse.builder()
                .countQuesCorrect(countQuesCorrect)
                .score(score)
                .build();
    }

    private boolean nextCurrentPart(ExamAttempt attempt,
                                          List<Long> partIdList, Long lastPartId) {
        Long currentPart = attempt.getCurrentPart().getId();
        if (Objects.equals(currentPart, lastPartId)) {
            attempt.setStatus(ATTEMPTSTATUS.SUBMITTED.name());
            attempt.setSubmitTime(LocalDateTime.now());
            return true;
        }
        int currentIndex = partIdList.indexOf(currentPart);
        Long nextPartId = partIdList.get(currentIndex + 1);
        attempt.setCurrentPart(partService.getReferencePart(nextPartId));
        return false;
    }

    private GradingResultResponse convertScore(String partName, String level, double scorePart, double totalScore,
                                               boolean isLastPass, boolean isPassed) {
        ScoreFailResponse scoring = JLPTRules.getScoring(level);
        PassFailResponse pass = PassFailResponse.builder()
                .partName(partName)
                .scorePart(scorePart)
                .totalScore(totalScore)
                .getScoring(scoring)
                .isLastPass(isLastPass)
                .isPass(isPassed) // trước đó dính liệt chưa
                .build();
        // Gọi Method xử lý
        return  gradingStrategy.evaluate(pass);
    }
}
