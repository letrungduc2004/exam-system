package com.example.exam_system.features.question.entity;


import com.example.exam_system.features.exam.entity.ExamPart;
import com.example.exam_system.features.exam_attempt.entity.StudentResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String content;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "score_weight", nullable = false)
    Double scoreWeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    ExamPart examPart;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Option> options = new ArrayList<>();
//    @OneToMany(mappedBy = "question")
//    List<StudentResponse> studentResponses = new ArrayList<>();
}
