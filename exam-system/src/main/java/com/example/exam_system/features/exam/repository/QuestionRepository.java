package com.example.exam_system.features.exam.repository;

import com.example.exam_system.features.exam.dto.response.QuestionFieldResponse;
import com.example.exam_system.features.exam.dto.response.QuestionHistoryResponse;
import com.example.exam_system.features.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
// BJT Logic
    // Lấy các Question dựa vào List ID của các Part
    @Query("select new com.example.exam_system.features.exam.dto.response.QuestionFieldResponse(q.id, q.content, q.examPart.id) "+
            "from Question q " +
            "where q.examPart.id in :partId " +
            "order by q.id asc ")
    List<QuestionFieldResponse> getQuestionByPartId(@Param("partId") List<Long> partId);

// JLPT Logic
    // Lấy các Question dựa vào ID của ExamPart

    // Kiểm tra câu hỏi nằm trong part hiện tại
    @Query("select count(que) from Question que " +
            "where que.id in :questionId " +
            "and que.examPart.id !=:partId")
    Long countQuestionInvalid(Long partId, List<Long> questionId);


//    private Long questionId;
//    private String questionContent;
//    private String explaining;
    @Query("select new com.example.exam_system.features.exam.dto.response.QuestionHistoryResponse(q.id, q.content, ex.explanation ,q.examPart.id) "+
            "from Question q " +
            "join Explanation ex on ex.question.id = q.id " +
            "where q.examPart.id in :partId " +
            "order by q.id asc ")
    List<QuestionHistoryResponse> getQuestionForHistory(@Param("partId") List<Long> partId);
}
