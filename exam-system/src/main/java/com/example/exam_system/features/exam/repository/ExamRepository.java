package com.example.exam_system.features.exam.repository;

import com.example.exam_system.features.exam.dto.response.ExamFieldResponse;
import com.example.exam_system.features.exam.dto.response.ExamHistoryResponse;
import com.example.exam_system.features.exam.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    // Lấy danh sách bài kiểm tra gồm: tiêu đề, mô tả, cấp độ, loại bài, giá, sao, số lượt thi, thời gian thi
    @Query("select e from Exam e " +
            "where (:examType is null or e.examType in :examType) " +
            "and (:difficulty is null or e.difficulty in :difficulty ) " +
            "and (:level is null or e.level in :level ) " +
            "and (:priceFrom is null or e.price >=:priceFrom ) " +
            "and (:priceTo is null or e.price <=:priceTo )")
    Page<Exam> getExam(List<String> examType, List<Integer> difficulty, List<String> level,
                       Double priceFrom, Double priceTo, Pageable pageable);

    @Query("select new com.example.exam_system.features.exam.dto.response.ExamFieldResponse(e.publicId, e.title, e.examType, e.duration, e.level) " +
            "from Exam e " +
            "where e.publicId =:publicId")
    Optional<ExamFieldResponse> getExamForAttempt(@Param("publicId") UUID publicId);


    @Query("select new com.example.exam_system.features.exam.dto.response.ExamHistoryResponse(e.publicId, e.title, e.examType, e.level) " +
            "from Exam e " +
            "where e.publicId =:publicId")
    Optional<ExamHistoryResponse> getExamForHistory(@Param("publicId") UUID publicId);

    Optional<Exam> findByPublicId(UUID publicExamId);
    boolean existsByTitle(String title);

    // QUERY 3: Lấy random 3 bài thi liên quan trên hệ thống
    @Query("SELECT e FROM Exam e WHERE e.publicId <> :publicExamId ORDER BY function('RAND')")
    List<Exam> getRandomRelatedExams(@Param("publicExamId") UUID publicExamId, Pageable pageable);
}
