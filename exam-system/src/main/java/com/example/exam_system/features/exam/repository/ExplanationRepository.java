package com.example.exam_system.features.exam.repository;

import com.example.exam_system.features.exam.entity.Explanation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExplanationRepository extends JpaRepository<Explanation, Long> {
    @Query("select qe from Explanation qe where qe.question.id in :questionIds")
    List<Explanation> findByQuestionIdIn(@Param("questionIds") List<Long> questionIds);
}
