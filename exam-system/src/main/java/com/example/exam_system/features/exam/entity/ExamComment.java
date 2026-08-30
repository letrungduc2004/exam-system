package com.example.exam_system.features.exam.entity;
import com.example.exam_system.features.account.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "exam_comments")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String comment;

    private Integer ratingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
}
