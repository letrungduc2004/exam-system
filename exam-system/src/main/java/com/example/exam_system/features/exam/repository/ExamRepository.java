package com.example.exam_system.features.exam.repository;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam.repository.projection.ExamProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

    Optional<Exam> findByTitle(String title);

    // Field by: exam_type, price(from..to) ,difficulty
    @Query("select e.id as id, e.title as title, e.examType as examType, " +
            "e.duration as duration, e.difficulty as difficulty, " +
            "e.numberTimes as numberTimes, e.price as price from Exam e where " +
            "(:type is null or e.examType =:type) " +
            "and (:difficulty is null or e.difficulty =: difficulty) " +
            "and (:priceMin is null or e.price >=:priceMin) " +
            "and (:priceMax is null or e.price <=:priceMax)")
    Page<ExamProjection> findExam(@Param("type") String examType,
                                  @Param("difficulty") Integer difficulty,
                                  @Param("priceMin") Double priceFrom,
                                  @Param("priceMax") Double priceTo, Pageable pageable);

}
