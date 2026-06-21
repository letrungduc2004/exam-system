package com.example.exam_system.features.exam_attempt.pattern.strategy;

import com.example.exam_system.features.exam_attempt.dto.StudentAnswerRequest;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import com.example.exam_system.features.account.entity.User;

import java.util.List;

public interface ExamStrategy {
    ExamAttempt startExam(Exam examId, User userId);
    void autoSave(ExamAttempt attempt, List<StudentAnswerRequest> request);
    ExamAttempt submitExam(ExamAttempt attempt);
    String examType();
}
