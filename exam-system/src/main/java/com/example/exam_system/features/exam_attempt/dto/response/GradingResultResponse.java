package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradingResultResponse {
    private Long countQuesCorrect;
    private Double score;

    private boolean isPassed;
    private String message;
}
