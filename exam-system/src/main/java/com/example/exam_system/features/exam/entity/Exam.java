package com.example.exam_system.features.exam.entity;

import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "exams")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 255, nullable = false)
    String title;

    @Column(name = "exam_type", nullable = false)
    String examType;

    @Column(name = "total_duration")
    Integer duration;

    @Column(name = "total_max_score")
    Double maxScore;

    @Column(nullable = false)
    Integer difficulty;

    @Column(nullable = false)
    Double price;

    @Column(name = "number_times")
    Integer numberTimes;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    String discription;

    @OneToMany(mappedBy = "exam", cascade =  CascadeType.REMOVE, orphanRemoval = true)
    List<ExamPart> examParts = new ArrayList<>();

    @OneToMany(mappedBy = "exam")
    List<ExamAttempt> examAttempts = new ArrayList<>();
}
