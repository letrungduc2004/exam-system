package com.example.exam_system.features.exam_attempt.pattern.impl;

import com.example.exam_system.common.rules.BJTRules;
import com.example.exam_system.features.exam_attempt.dto.response.ScoreRangeResponse;
import com.example.exam_system.common.enums.ATTEMPTSTATUS;
import com.example.exam_system.common.enums.EXAMTYPE;
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
import com.example.exam_system.features.exam_attempt.dto.response.ScaledScoreResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.exam_attempt.entity.StudentAnswer;
import com.example.exam_system.features.exam_attempt.pattern.grading.GradingStrategy;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import com.example.exam_system.features.exam_attempt.repository.ExamAttemptRepository;
import com.example.exam_system.features.exam_attempt.repository.StudentAnswerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BJTPattern implements ExamStrategy {

    private final GradingStrategy<ScaledScoreResponse> gradingStrategy;
    //private final ScaledScoreGradingImpl scaledScoreGrading;
    private final ExamAttemptRepository examAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamService examService;
    private final ExamPartService examPartService;
    private final OptionService optionService;
    private final QuestionService questionService;


    @Override
    public String examType() {
        return EXAMTYPE.BJT.name();
    }

    @Override
    public ExamFieldResponse getQuestion(ExamAttempt attempt) {
        List<ExamPart> getAllPart = examPartService.getAllPartExam(attempt.getExam().getId());
        List<Long> allPartId = getAllPart.stream().map(value -> value.getId())
                .collect(Collectors.toList());
        return examService.getQuestionTest(attempt.getExam().getPublicId(), allPartId);
    }

    @Override
    public ExamAttempt startExam(User user, Exam exam) {
        ExamAttempt attempt = ExamAttempt.builder()
                .startTime(LocalDateTime.now())
                .submitTime(null)
                .status(ATTEMPTSTATUS.STARTED.name())
                .currentPart(null)
                .correctAnswersCount(0L)
                .totalScore(0.0)
                .user(user)
                .exam(exam)
                .build();
        examAttemptRepository.save(attempt);
        return attempt;
    }

    @Override
    public void autoSaveAnswer(ExamAttempt examAttempt, List<AnswerRequest> studentAnswer) {
        // Tư tưởng: Cứ sau mỗi 30s thì FE sẽ gửi câu hỏi xuống để lưu
        // B1: Lấy danh sách tất cả câu hỏi mà Student chọn
        // B2: Nếu câu hỏi đã lưu trong StudentResponse ? Update : Create

        List<Long> questionId = studentAnswer.stream().map(value -> value.getQuestionId())
                .collect(Collectors.toList());

        List<StudentAnswer> getQuestion = studentAnswerRepository.getAllQuestionAnswer(examAttempt.getId(), questionId);
        Map<Long, StudentAnswer> mappingQuestion = new HashMap<>();
        for (StudentAnswer request : getQuestion) {
            mappingQuestion.put(request.getQuestion().getId(), request);
        }
        List<StudentAnswer> upSert = new ArrayList<>();
        for (AnswerRequest request : studentAnswer) {
            Long quesId = request.getQuestionId();
            Long optionId = request.getOptionId();
            StudentAnswer stAnswer = mappingQuestion.get(quesId);
            if (Objects.isNull(stAnswer)) {
                // Chưa có câu hỏi trong Map -> Create
                stAnswer = StudentAnswer.builder()
                        .examAttempt(examAttempt)
                        .updatedAt(LocalDateTime.now())
                        .question(questionService.getReferenceQuestion(quesId))
                        .selectedOption(optionService.getReferenceOption(optionId))
                        .build();
            } else {
                // Update
                stAnswer.setSelectedOption(optionService.getReferenceOption(optionId));
            }
            upSert.add(stAnswer);
        }
        studentAnswerRepository.saveAll(upSert);
    }

    @Override
    public ExamAttempt submitExam(ExamAttempt attempt) {
        GradingResultResponse calculate = calculateScore(attempt);
        attempt.setStatus(ATTEMPTSTATUS.SUBMITTED.name());
        attempt.setSubmitTime(LocalDateTime.now());
        attempt.setCorrectAnswersCount(calculate.getCountQuesCorrect());
        attempt.setTotalScore(calculate.getScore());
        attempt.setIsPassed(calculate.isPassed());
        attempt.setMessage(calculate.getMessage());
        examAttemptRepository.save(attempt);
        return attempt;
    }

    @Override
    public GradingResultResponse calculateScore(ExamAttempt attempt) {
        // Tư tưởng: Cập nhật trạng thái bài thi tránh Double Submit
        // Lấy toàn bộ câu hỏi trong bài kiểm tra theo examId
        // Lấy toàn bộ câu hỏi mà người dùng chọn trong bài kiểm tra đó =>
        // Map<questionId, Option>
        // So sánh đối chiếu 2 option trong theo questionId
        List<QuestionProjection> getQuestionCorrect = optionService.getQuestionByExamId(attempt.getExam().getId());

        List<StudentAnswer> getQuestionAnswer = studentAnswerRepository.getAllQuestionBJT(attempt.getId());
        Map<Long, Long> mappingQuestionAns = getQuestionAnswer.stream()
                .collect(Collectors.toMap(
                        value -> value.getQuestion().getId(),
                        value -> value.getSelectedOption().getId()));

        // Duyệt toàn bộ câu hỏi lấy ra từ đề thi và đối chiếu câu hỏi người dùng chọn
        long countQuesCorrect = 0;
        double score = 0.0;
        for (QuestionProjection request : getQuestionCorrect) {
            Long quesIdCorrect = request.getQuestionId();
            Long optionIdCorrect = request.getOptionId();
            Long optionIdAnswer = mappingQuestionAns.get(quesIdCorrect);
            if (!Objects.isNull(optionIdAnswer) && Objects.equals(optionIdAnswer, optionIdCorrect)) {
                // Với Wrapper: == so sánh địa chỉ, equal so sánh giá trị
                score += request.getScoreWeight();
                countQuesCorrect++;
            }
        }

        // Xử lý quy đổi level: toàn bộ list<BJTRules>
        List<ScoreRangeResponse> getScoring = BJTRules.getScoring();
        ScaledScoreResponse convert = ScaledScoreResponse.builder()
                .scoreRaw(score)
                .getScoring(getScoring)
                .build();

        GradingResultResponse response = gradingStrategy.evaluate(convert);
        return GradingResultResponse.builder()
                .countQuesCorrect(countQuesCorrect)
                .score(score)
                .isPassed(response.isPassed())
                .message(response.getMessage())
                .build();
    }
}
