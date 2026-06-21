package com.example.exam_system.features.exam.dto;

import com.example.exam_system.common.enums.ExamType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {
    private Long id;
    private String title;
    private ExamType examType;
    private Integer duration;
    private String difficulty;
    private Integer numberTimes;
    private Double price;
}
