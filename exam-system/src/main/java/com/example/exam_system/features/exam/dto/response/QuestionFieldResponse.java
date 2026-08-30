package com.example.exam_system.features.exam.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"questionId", "questionContent", "explaining", "optionList"})
public class QuestionFieldResponse {
    private Long questionId;
    private String questionContent;
    private List<OptionFieldResponse> optionList;

    @JsonIgnore
    private Long partId;

    public QuestionFieldResponse(Long questionId, String questionContent, Long partId) {
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.partId = partId;
    }
}
