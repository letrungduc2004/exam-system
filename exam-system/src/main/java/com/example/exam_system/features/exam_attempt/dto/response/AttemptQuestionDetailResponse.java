package com.example.exam_system.features.exam_attempt.dto.response;

import com.example.exam_system.features.exam.dto.response.QuestionHistoryResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonPropertyOrder({"questionId", "questionContent", "explaining", "idOptionChoice", "optionList"})
public class AttemptQuestionDetailResponse extends QuestionHistoryResponse {
    private Long idOptionChoice;
}
