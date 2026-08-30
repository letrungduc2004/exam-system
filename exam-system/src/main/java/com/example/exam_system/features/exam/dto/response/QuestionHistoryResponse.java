package com.example.exam_system.features.exam.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonPropertyOrder({"questionId", "questionContent", "explaining", "optionList"})
public class QuestionHistoryResponse {
    private Long questionId;
    private String questionContent;
    private String explaining;
    private List<OptionHistoryResponse> optionList;

    @JsonIgnore
    private Long partId;

    public QuestionHistoryResponse(Long questionId, String questionContent, String explaining, Long partId) {
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.explaining = explaining;
        this.partId = partId;
    }
}
