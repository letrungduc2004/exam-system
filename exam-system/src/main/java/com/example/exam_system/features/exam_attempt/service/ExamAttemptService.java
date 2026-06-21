package com.example.exam_system.features.exam_attempt.service;

import com.example.exam_system.features.exam_attempt.dto.StudentAnswerRequest;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.common.enums.ExamStatus;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.exam_attempt.pattern.factory.ExamFactory;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import com.example.exam_system.features.exam_attempt.repository.ExamAttemptRepository;
import com.example.exam_system.features.exam.repository.ExamRepository;
import com.example.exam_system.features.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamAttemptService {
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final ExamFactory examFactory;


    public ExamAttempt startExam(Integer examId, UUID userId) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        ExamStrategy examType = examFactory.getExamType(existingExam.getExamType());
        ExamAttempt attempt = examType.startExam(existingExam, existingUser);

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoSave(Long attemptId, List<StudentAnswerRequest> request) {
        ExamAttempt existingAttempt = examAttemptRepository.findById(attemptId)
                    .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        String examStatus = existingAttempt.getStatus();
        if(!Objects.isNull(examStatus) && !examStatus.equals(ExamStatus.STARTED)){
             throw new AppException(ErrorCode.EXAM_ATTEMPT_SUBMIT);
        }

        ExamStrategy examType = examFactory.getExamType(existingAttempt.getExam().getExamType());
        examType.autoSave( existingAttempt, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public ExamAttempt submitExam(Long attemptId, List<StudentAnswerRequest> request) {
        ExamAttempt existingAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        String examStatus = existingAttempt.getStatus();
        if(!Objects.isNull(examStatus) && !examStatus.equals(ExamStatus.STARTED)){
            throw new AppException(ErrorCode.EXAM_ATTEMPT_SUBMIT);
        }
        Exam exam = existingAttempt.getExam();
        ExamStrategy examType = examFactory.getExamType(existingAttempt.getExam().getExamType());

        // Lưu các câu hỏi còn sót lại trước khi submit
        examType.autoSave( existingAttempt, request);

        ExamAttempt attempt =  examType.submitExam(existingAttempt);

        return null;
    }
}
