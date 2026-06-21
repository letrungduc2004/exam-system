package com.example.exam_system.features.exam_attempt.pattern.impl;

import com.example.exam_system.features.exam_attempt.dto.StudentAnswerRequest;
import com.example.exam_system.common.enums.ExamStatus;
import com.example.exam_system.common.enums.ExamType;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam.repository.ExamRepository;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.exam_attempt.entity.StudentResponse;
import com.example.exam_system.features.exam_attempt.repository.ExamAttemptRepository;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import com.example.exam_system.features.exam_attempt.repository.StudentResponseRepository;
import com.example.exam_system.features.question.entity.Option;
import com.example.exam_system.features.question.repository.OptionRepository;
import com.example.exam_system.features.question.repository.QuestionRepository;
import com.example.exam_system.features.exam_attempt.repository.projection.CorrectAnswerProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GLobalExamExecutor implements ExamStrategy {
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamRepository examRepository;
    private final StudentResponseRepository studentResponseRepository;
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;

    // Chiến lược dành riêng bài thi V_ACT - thi liền một mạch 150 phút
    @Override
    public ExamAttempt startExam(Exam examId, User userId) {
        ExamAttempt examAttempt = ExamAttempt.builder()
                .startTime(LocalDateTime.now())
                .status(ExamStatus.STARTED.name())
                .currentPart(null)
                .exam(examId)
                .user(userId)
                .build();
        examAttemptRepository.save(examAttempt);
        return examAttempt;
    }

    @Override
    public void autoSave(ExamAttempt attempt, List<StudentAnswerRequest> request) {
        // Tư tường: 1. Gom hết ID đáp án câu hỏi của Student cho vào map
        // 2. Từ ID đó lấy ra danh sách câu hỏi mà student đã làm, cho vào Map<idQuestion, StudentAnser>
        // 3. Kiểm tra id đã tồn tại trong map chưa
        // 3.1: Đã tồn tại => update câu hỏi
        // 3.2: Chưa tồn tại => Insert

        List<Long> questionMap = request.stream()
                .map(x -> x.getQuestionId())
                .collect(Collectors.toList());

        List<StudentResponse> getAnswer = studentResponseRepository.findQuestion(attempt.getId(), questionMap);

        Map<Long, StudentResponse> mappingAnswer = new HashMap<>();
        for (StudentResponse st : getAnswer) {
            mappingAnswer.put(st.getQuestion().getId(), st);
        }

        List<StudentResponse> upSertAnswer =  new ArrayList<>();
        for (StudentAnswerRequest answerRequest : request) {
            StudentResponse response = null;
            Long questionId = answerRequest.getQuestionId();
            Long optionChoose = answerRequest.getOptionId();
            if(mappingAnswer.containsKey(questionId)){
              // Update option
                response = mappingAnswer.get(answerRequest.getQuestionId());
                Option newOption = optionRepository.getReferenceById(optionChoose);
                response.setSelectedOption(newOption);
            }else{
                // Insert
                response = new StudentResponse();
                response.setQuestion(questionRepository.getReferenceById(questionId));
                response.setSelectedOption(optionRepository.getReferenceById(optionChoose));
            }
            upSertAnswer.add(response);
        }
        studentResponseRepository.saveAll(upSertAnswer);
    }

    @Override
    public ExamAttempt submitExam(ExamAttempt attempt) {
        // Tư tưởng chấm điểm & Submit
        // 1. Lấy ra toàn bộ câu hỏi student làm trong đề thi cho vào Map<questionId, optionID>
        // 2. Lấy ra toàn bộ câu hỏi trong đề thi cho vào Map<questionId, optionID>
        // 3. So sánh 2 map rồi tính điểm

        List<StudentResponse> findAnswerQues = studentResponseRepository.findQuestionByAttempt(attempt.getId());
        Map<Long, Long> answerMap = new HashMap<>();
        for(StudentResponse st : findAnswerQues){
            answerMap.put(st.getQuestion().getId(), st.getSelectedOption().getId());
        }

        List<CorrectAnswerProjection> findCorrectQues = questionRepository.getQuestionByExamId(attempt.getExam().getId());
        Map<Long, Long> correctMap = new HashMap<>();
        for(CorrectAnswerProjection correct : findCorrectQues){
            correctMap.put(correct.getQuestionId(), correct.getOptionId());
        }

        Long countCorrect = 0L;
        for(Map.Entry<Long, Long> compareAnswer : correctMap.entrySet()){
            // Map có tốc độ truy xuất key O(1);
            Long questionId = compareAnswer.getKey();
            Long optionId = compareAnswer.getValue();
            Long optionChoose = answerMap.get(questionId);
           if(!Objects.isNull(optionChoose) && optionChoose.equals(optionId)){
               countCorrect++;
           }
        }

        double score = (countCorrect / (double) findCorrectQues.size()) * 10;

        attempt.setSubmitTime(LocalDateTime.now());
        attempt.setStatus(ExamStatus.SUBMITTED.name());
        attempt.setCurrentPart(null);
        attempt.setTotalScore(score);

        return attempt;
    }


    @Override
    public String examType() {
        return ExamType.V_ACT.name();
    }
}
