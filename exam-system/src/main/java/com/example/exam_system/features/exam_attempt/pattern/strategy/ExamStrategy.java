package com.example.exam_system.features.exam_attempt.pattern.strategy;

import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.exam.dto.response.ExamFieldResponse;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam_attempt.dto.request.AnswerRequest;
import com.example.exam_system.features.exam_attempt.dto.response.GradingResultResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;

import java.util.List;
import java.util.UUID;

public interface ExamStrategy {
    // Hiển thị danh sách câu hỏi cho bài thi
    ExamFieldResponse getQuestion(ExamAttempt attempt);
    // Lấy ra loại bài thi
    String examType();
    ExamAttempt startExam(User existingUser, Exam existingExam);
    void autoSaveAnswer(ExamAttempt examAttempt, List<AnswerRequest> studentAnswer);
    ExamAttempt submitExam(ExamAttempt examAttempt);
    GradingResultResponse calculateScore(ExamAttempt attempt);
}
