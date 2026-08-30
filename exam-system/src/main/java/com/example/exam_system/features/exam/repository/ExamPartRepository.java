package com.example.exam_system.features.exam.repository;

import com.example.exam_system.features.exam.dto.response.PartFieldResponse;
import com.example.exam_system.features.exam.dto.response.PartDetailResponse;
import com.example.exam_system.features.exam.dto.response.PartHistoryResponse;
import com.example.exam_system.features.exam.entity.ExamPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamPartRepository extends JpaRepository<ExamPart, Long> {

    @Query("select new com.example.exam_system.features.exam.dto.response.PartFieldResponse(ep.id, ep.name, ep.duration) " +
            "from ExamPart ep " +
            "where ep.id in :partId ")
    List<PartFieldResponse> getAllExamPart(@Param("partId") List<Long> partId);

    List<ExamPart> findByExamIdOrderByOrderIndexAsc(Long examId);

    @Query("SELECT new com.example.exam_system.features.exam.dto.response.PartDetailResponse(" +
           "ep.id, ep.name, ep.duration, COUNT(q.id), ROUND(COALESCE(SUM(q.scoreWeight), 0.0), 2)) " +
           "FROM ExamPart ep " +
           "LEFT JOIN ep.questions q " +
           "WHERE ep.exam.publicId = :publicExamId " +
           "GROUP BY ep.id, ep.name, ep.duration, ep.orderIndex " +
           "ORDER BY ep.orderIndex ASC")
    List<PartDetailResponse> getExamPartSummariesByPublicId(@Param("publicExamId") UUID publicExamId);

    @Query("select new com.example.exam_system.features.exam.dto.response.PartHistoryResponse(ep.id, ep.name, ep.duration) " +
            "from ExamPart ep " +
            "where ep.exam.publicId =:publicExamId ")
    List<PartHistoryResponse> getPartHistory(@Param("publicExamId") UUID publicExamId);
}
