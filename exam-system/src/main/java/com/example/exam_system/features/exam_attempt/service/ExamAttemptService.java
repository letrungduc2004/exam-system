package com.example.exam_system.features.exam_attempt.service;

import com.example.exam_system.common.enums.ATTEMPTSTATUS;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.service.UserService;
import com.example.exam_system.features.exam.dto.response.ExamFieldResponse;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam.service.ExamService;
import com.example.exam_system.features.exam_attempt.dto.request.AnswerRequest;
import com.example.exam_system.features.exam_attempt.dto.request.AttemptStartRequest;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptStartResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptSubmitResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.exam_attempt.entity.StudentAnswer;
import com.example.exam_system.features.exam_attempt.mapper.AttemptExamMapping;
import com.example.exam_system.features.exam_attempt.pattern.factory.ExamFactory;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import com.example.exam_system.features.exam_attempt.repository.ExamAttemptRepository;
import com.example.exam_system.features.exam_attempt.repository.StudentAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ExamAttemptService {
    private final ExamAttemptRepository examAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final UserService userService;
    private final ExamService examService;
    private final ExamFactory examFactory;
    private final AttemptExamMapping attemptMapping;

    @Transactional(rollbackFor = Exception.class)
    public AttemptStartResponse startExam(AttemptStartRequest request) {
        // Kiểm tra danh tính User và Bài thi
        Exam exam = examService.existingExam(request.getPublicExamId());
        User user = userService.existingUser(request.getUserId());

        // Tìm chiến lược phù hợp cho bài thi
        ExamStrategy typeStrategy = examFactory.strategyType(exam.getExamType());
        ExamAttempt attempt = typeStrategy.startExam(user, exam);
        return attemptMapping.toExamAttemptResponse(attempt);
    }

    @Transactional(rollbackFor = Exception.class)
    public ExamFieldResponse getQuestion(UUID publicAttemptId) {
        // Kiểm tra danh tính phiên thi, bài thi và User
        ExamAttempt attempt = examAttemptRepository.findByPublicId(publicAttemptId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        // Kiểm tra nếu bài thi không ở trạng thái STARTED thì không cho lấy câu hỏi
        validateAttemptInProgress(attempt);

        // Tìm chiến lược phù hợp cho bài thi
        ExamStrategy typeStrategy = resolveStrategy(attempt);
        ExamFieldResponse question = typeStrategy.getQuestion(attempt);
        return question;
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoSaveQuestion(UUID publicAttemptId, List<AnswerRequest> studentAnswer) {
        // Kiểm tra danh tính phiên thi, bài thi
        ExamAttempt attempt = examAttemptRepository.findByPublicId(publicAttemptId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        // Kiểm tra nếu bài thi không ở trạng thái STARTED thì không cho submit Question
        validateAttemptInProgress(attempt);

        // Tìm chiến lược phù hợp cho bài thi
        ExamStrategy typeStrategy = resolveStrategy(attempt);
        typeStrategy.autoSaveAnswer(attempt, studentAnswer);
    }

    @Transactional(rollbackFor = Exception.class)
    public AttemptSubmitResponse submitExam(UUID publicAttemptId, List<AnswerRequest> studentAnswer) {
        // Kiểm tra danh tính phiên thi, bài thi
        ExamAttempt attempt = examAttemptRepository.findByPublicId(publicAttemptId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        // Kiểm tra nếu bài thi không ở trạng thái STARTED thì không cho nộp
        validateAttemptInProgress(attempt);

        // Tìm chiến lược phù hợp cho bài thi
        ExamStrategy typeStrategy = resolveStrategy(attempt);
        // Lưu các đáp án còn sót lại
        typeStrategy.autoSaveAnswer(attempt, studentAnswer);

        ExamAttempt submit = typeStrategy.submitExam(attempt);
        return attemptMapping.toAttemptSubmitResponse(submit);
    }

    private ExamStrategy resolveStrategy(ExamAttempt attempt) {
        Exam exam = examService.existingExam(attempt.getExam().getPublicId());
        return examFactory.strategyType(exam.getExamType());
    }

    private void validateAttemptInProgress(ExamAttempt attempt) {
        String status = attempt.getStatus();
        if (status != null && !status.equals(ATTEMPTSTATUS.STARTED.name())) {
            throw new AppException(ErrorCode.EXAM_ATTEMPT_SUBMIT);
        }
    }
}
