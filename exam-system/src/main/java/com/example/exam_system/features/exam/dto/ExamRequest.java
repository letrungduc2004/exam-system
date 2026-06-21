package com.example.exam_system.features.exam.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRequest {
    private String title;

    private String examType;
    private Integer duration;
    private Double maxScore;
    private Integer difficulty;
    private Double price;
    private String discription;
}
