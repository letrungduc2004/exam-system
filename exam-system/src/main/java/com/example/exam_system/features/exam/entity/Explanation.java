package com.example.exam_system.features.exam.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "question_explanations")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Explanation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "explanation")
    String explanation;

    // 1 câu hỏi có 1 giải thích
    // 1 giải thích tương ưng 1 câu hỏi
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    Question question;
}
