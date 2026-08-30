package com.example.exam_system.features.exam_attempt.repository;

import com.example.exam_system.features.exam.repository.projection.QuestionProjection;
import com.example.exam_system.features.exam_attempt.entity.StudentAnswer;
import com.example.exam_system.features.exam_attempt.repository.projection.AnswerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
//BJT + JLPT
    // Lấy danh sách toàn bộ câu hỏi mà Student đã chọn trong phiên thi
    @Query("select an from StudentAnswer an " +
            "where an.examAttempt.id =:attemptId " +
            "and an.question.id in :questionId ")
    List<StudentAnswer> getAllQuestionAnswer(Long attemptId, List<Long> questionId);


// BJT
    // Lấy toàn bộ câu hỏi mà người dùng đã chọn trong phiên thi(examAttempt) -- done
    @Query("select an from StudentAnswer an " +
        "where an.examAttempt.id =:attemptId ")
    List<StudentAnswer> getAllQuestionBJT(Long attemptId);


// JLPT
   //  Lấy toàn bộ câu hỏi mà người dùng đã chọn trong phiên thi(examAttempt) và partId
    @Query("select an.question.id as questionId, an.selectedOption.id as optionId " +
            " from StudentAnswer an " +
            "join an.question ques " +
            "join ques.examPart exPa " +
            "where an.examAttempt.id =:attemptId " +
            "and exPa.id =:partId ")
    List<AnswerProjection> getAllQuestionJLPT(Long attemptId, Long partId);
}
