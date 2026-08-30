package com.example.exam_system.features.exam.repository;

import com.example.exam_system.features.exam.dto.response.ExamCommentResponse;
import com.example.exam_system.features.exam.entity.ExamComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamCommentRepository extends JpaRepository<ExamComment, Long> {

    // 4 QUERY 4: Lấy danh sách đánh giá của người dùng về bài thi theo examId bằng DTO PROJECTION
    @Query("SELECT new com.example.exam_system.features.exam.dto.response.ExamCommentResponse(" +
           "us.fullName, c.comment, c.ratingCount) " +
           "FROM ExamComment c " +
            "JOIN c.user us " +
           "WHERE c.exam.publicId = :publicExamId " +
           "ORDER BY c.id DESC")
    List<ExamCommentResponse> getCommentsByExamPublicId(@Param("publicExamId") UUID publicExamId);
}