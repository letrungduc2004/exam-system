package com.example.exam_system.features.exam.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class OptionHistoryResponse {
    private Long optionId;
    private String optionContent;
    private boolean isCorrect;

    @JsonIgnore
    private Long questionId;

    public OptionHistoryResponse(Long optionId, String optionContent, boolean isCorrect, Long questionId) {
        this.optionId = optionId;
        this.optionContent = optionContent;
        this.isCorrect = isCorrect;
        this.questionId = questionId;
    }
}
