package com.example.exam_system.features.exam_attempt.service;

import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.service.UserService;
import com.example.exam_system.features.exam.dto.response.ExamHistoryResponse;
import com.example.exam_system.features.exam.dto.response.OptionHistoryResponse;
import com.example.exam_system.features.exam.dto.response.PartHistoryResponse;
import com.example.exam_system.features.exam.dto.response.QuestionHistoryResponse;
import com.example.exam_system.features.exam.service.ExamService;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptDetailResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptHistoryResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptQuestionDetailResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.exam_attempt.entity.StudentAnswer;
import com.example.exam_system.features.exam_attempt.mapper.AttemptExamMapping;
import com.example.exam_system.features.exam_attempt.mapper.AttemptHistoryMapping;
import com.example.exam_system.features.exam_attempt.repository.ExamAttemptRepository;
import com.example.exam_system.features.exam_attempt.repository.StudentAnswerRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamHistoryService {
    private final ExamService examService;
    private final UserService userService;
    private final AttemptHistoryMapping historyMapping;
    private final AttemptExamMapping attemptExamMapping;
    private final ExamAttemptRepository examAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    @Transactional(rollbackFor = Exception.class)
    public Page<AttemptHistoryResponse> getAttemptHistory(UUID userId, Pageable pageable) {
        User user = userService.existingUser(userId);
        Page<ExamAttempt> attempt = examAttemptRepository.findByUserId(userId, pageable);
        List<AttemptHistoryResponse> result =  historyMapping.toAttemptListHistory(attempt.getContent());
        return new PageImpl<>(result, attempt.getPageable(), attempt.getTotalElements());
    }

    @Transactional(readOnly = true)
    public AttemptDetailResponse getDetailAttemptHistory(UUID attemptPublicId) {
        // 1. Lấy thông tin phiên làm bài
        ExamAttempt attempt = examAttemptRepository.findByPublicId(attemptPublicId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        // 2. Lấy thông tin chi tiết bài thi từ ExamService
        ExamHistoryResponse examHistory = examService.getExamHistoryInformation(attempt.getExam().getPublicId());

        // 3. Lấy câu trả lời của học sinh
        List<StudentAnswer> questionAnswer = studentAnswerRepository.getAllQuestionBJT(attempt.getId());
        Map<Long, Long> mappingQuestion = questionAnswer.stream().collect(Collectors.toMap(
                x -> x.getQuestion().getId(),
                x -> x.getSelectedOption().getId()
        ));

        for (PartHistoryResponse part : examHistory.getPart()) {
            List<QuestionHistoryResponse> result = new ArrayList<>();
            for (QuestionHistoryResponse q : part.getQuestion()) {
                AttemptQuestionDetailResponse response = AttemptQuestionDetailResponse.builder()
                        .questionId(q.getQuestionId())
                        .questionContent(q.getQuestionContent())
                        .explaining(q.getExplaining())
                        .optionList(q.getOptionList())
                        .idOptionChoice(mappingQuestion.get(q.getQuestionId()))
                        .partId(q.getPartId())
                        .build();
                result.add(response);
            }
        }

        return AttemptDetailResponse.builder()
                .publicAttemptId(attempt.getPublicId())
                .totalTime(historyMapping.definedSubmitTime(attempt))
                .totalScore(historyMapping.definedTotalScore(attempt))
                .correctAnswersCount(attemptExamMapping.definedCorrectCount(attempt))
                .isPassed(attempt.getIsPassed())
                .message(attempt.getMessage())

                .publicExamId(examHistory.getPublicExamId())
                .title(examHistory.getTitle())
                .examType(examHistory.getExamType())
                .level(examHistory.getLevel())
                .part(examHistory.getPart())
                .build();
    }

}
