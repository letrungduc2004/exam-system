package com.example.exam_system.features.exam_attempt.entity;

import com.example.exam_system.features.exam.entity.ExamPart;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.exam.entity.Exam;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "exam_attempts")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "public_id", unique = true, updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    UUID publicId;

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "submit_time")
    LocalDateTime submitTime;

    @Column(name = "total_score", nullable = false)
    Double totalScore;

    @Column(name = "status")
    String status ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "correct_count")
    Long correctAnswersCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_part_id")
    ExamPart currentPart;

    @Column(name = "is_passed")
    Boolean isPassed;

    @Column(name = "message")
    String message;

    @OneToMany(mappedBy = "examAttempt", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<StudentAnswer> studentResponses = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID();
        }
    }
}
