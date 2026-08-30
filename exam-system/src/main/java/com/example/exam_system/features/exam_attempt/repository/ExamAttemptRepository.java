package com.example.exam_system.features.exam_attempt.repository;

import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    Optional<ExamAttempt> findByPublicId(UUID publicExamId);

    // Lấy toàn bộ danh sách bài kiểm tra mà người dùng đã làm
    @Query("select a from ExamAttempt a " +
            "where a.user.id =:userId and a.status ='SUBMITTED' ")
    Page<ExamAttempt> findByUserId(UUID userId,  Pageable pageable);

}
