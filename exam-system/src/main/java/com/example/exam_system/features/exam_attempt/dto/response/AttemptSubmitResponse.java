package com.example.exam_system.features.exam_attempt.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptSubmitResponse {
    private UUID publicId;
    private String examName;
    private LocalDateTime submitTime;
    private Double totalScore;
    private String correctAnswersCount;
    private Boolean isPassed;
    private String message;
}
