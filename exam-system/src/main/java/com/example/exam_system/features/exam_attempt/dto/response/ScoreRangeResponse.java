package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreRangeResponse {
    private String level;
    private Double scoreFrom;
    private Double scoreTo;
}
