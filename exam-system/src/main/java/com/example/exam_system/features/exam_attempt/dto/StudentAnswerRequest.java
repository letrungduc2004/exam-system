package com.example.exam_system.features.exam_attempt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswerRequest {
    Long questionId;
    Long optionId;
}
