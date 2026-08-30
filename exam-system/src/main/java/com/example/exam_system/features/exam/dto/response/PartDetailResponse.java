package com.example.exam_system.features.exam.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class PartDetailResponse {
    private Long partId;
    private String partName;
    private Integer duration;
    private Long totalQuestion;
    private Double maxScore;

    public PartDetailResponse(Long partId, String partName, Integer duration, Long totalQuestion, Double maxScore) {
        this.partId = partId;
        this.partName = partName;
        this.duration = duration;
        this.totalQuestion = totalQuestion;
        this.maxScore = maxScore;
    }
}
