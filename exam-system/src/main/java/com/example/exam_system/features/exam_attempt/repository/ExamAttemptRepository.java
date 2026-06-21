package com.example.exam_system.features.exam_attempt.repository;

import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
}
