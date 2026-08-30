package com.example.exam_system.features.exam_attempt.dto.response;

import com.example.exam_system.features.exam.dto.response.ExamHistoryResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonPropertyOrder({"publicAttemptId","publicExamId", "totalTime" ,"title",  "examType", "level",
        "totalScore", "correctAnswersCount", "isPassed", "message", "part"})
public class AttemptDetailResponse extends ExamHistoryResponse {
    private UUID publicAttemptId;
    private String totalTime;
    private String totalScore;
    private String correctAnswersCount;
    private Boolean isPassed;
    private String message;
}
