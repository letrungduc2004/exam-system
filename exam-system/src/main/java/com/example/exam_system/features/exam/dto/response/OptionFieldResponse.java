package com.example.exam_system.features.exam.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class OptionFieldResponse {
    private Long optionId;
    private String optionContent;

    @JsonIgnore
    private Long questionId;


    public OptionFieldResponse(Long optionId, String optionContent, Long questionId) {
        this.optionId = optionId;
        this.optionContent = optionContent;
        this.questionId = questionId;
    }
}
