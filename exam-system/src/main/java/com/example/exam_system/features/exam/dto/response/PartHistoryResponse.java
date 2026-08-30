package com.example.exam_system.features.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartHistoryResponse {
    private Long partId;
    private String partName;
    private Integer duration;
    private List<QuestionHistoryResponse> question;

    public PartHistoryResponse(Long partId, String partName, Integer duration) {
        this.partId = partId;
        this.partName = partName;
        this.duration = duration;
    }
}
