package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScaledScoreResponse {
    // Phục vụ cho quy đổi điểm sang level tương ứng
    private Double scoreRaw;
    List<ScoreRangeResponse> getScoring = new ArrayList<>();
}

