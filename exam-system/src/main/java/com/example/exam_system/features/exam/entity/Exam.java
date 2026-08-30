package com.example.exam_system.features.exam.entity;

import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "exams")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    // Exam, ExamPart, Question, Option chỉ thuộc về 1 bài thi

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "public_id", unique = true, updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    UUID publicId;

    @Column(length = 255, nullable = false)
    String title;

    @Column(name = "exam_type", nullable = false)
    String examType;

    @Column(name = "level")
    String level;

    @Column(name = "total_duration")
    Integer duration;

    @Column(nullable = false)
    Integer difficulty;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    String discription;

    @Column(name = "start")
    private Double start;

    @Column(name = "user_count")
    private Long userCount;

    @Column(name = "total_question")
    private Long totalQuestion;

    @Column(name = "total_max_score")
    private Long maxScore;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<ExamPart> examParts = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID();
        }
    }
}
