package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreFailResponse {
    private String level;
    // Điểm total tối thiếu Pass
    private Double passScore;
    // Điểm tối thiểu Liệt (part)
    private Double failScore;
}
