package com.example.exam_system.features.exam.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class PartFieldResponse {
    private Long partId;
    private String partName;
    private Integer partDuration;
    private List<QuestionFieldResponse> questionList;

    public PartFieldResponse(Long partId, String partName, Integer partDuration) {
        this.partId = partId;
        this.partName = partName;
        this.partDuration = partDuration;
    }
}
