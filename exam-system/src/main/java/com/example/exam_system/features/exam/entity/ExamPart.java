package com.example.exam_system.features.exam.entity;

import com.example.exam_system.features.question.entity.Question;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exam_parts")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    Integer duration;

    @Column(name = "order_index")
    Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    Exam exam;

    @OneToMany(mappedBy = "examPart", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Question> questions = new ArrayList<>();


    @OneToMany(mappedBy = "currentPart")
    List<ExamAttempt> examAttempts = new ArrayList<>();
}
