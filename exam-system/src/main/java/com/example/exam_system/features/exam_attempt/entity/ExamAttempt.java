package com.example.exam_system.features.exam_attempt.entity;

import com.example.exam_system.features.exam.entity.ExamPart;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.exam.entity.Exam;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "submit_time", nullable = false)
    LocalDateTime submitTime;

    @Column(name = "total_score", nullable = false)
    Double totalScore;

    @Column(name = "status")
    String status ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_part_id")
    ExamPart currentPart;

    @OneToMany(mappedBy = "examAttempt", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<StudentResponse> studentResponses = new ArrayList<>();
}
