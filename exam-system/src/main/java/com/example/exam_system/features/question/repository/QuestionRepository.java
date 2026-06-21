package com.example.exam_system.features.question.repository;

import com.example.exam_system.features.question.entity.Question;
import com.example.exam_system.features.exam_attempt.repository.projection.CorrectAnswerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Lấy toàn bộ câu hỏi trong đề thi theo examID
    @Query("select ques.id as questionId, op.id as optionId from Question ques " +
            "join ques.options op " +
            "join ques.examPart pa " +
            "join pa.exam ex " +
            "where ex.id =:examId and op.isCorrect = true")
    List<CorrectAnswerProjection> getQuestionByExamId(Long examId);
}
