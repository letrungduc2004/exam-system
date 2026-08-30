package com.example.exam_system.features.exam.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {
    private UUID publicExamId;
    private String title;
    private String examType;
    private Integer duration;
    private String level;
    private String difficulty;
    private String price;
    private String discription;
    private Double start;
    private String userCount;
}
