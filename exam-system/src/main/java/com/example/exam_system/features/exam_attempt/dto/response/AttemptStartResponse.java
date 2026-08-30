package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptStartResponse {
    private UUID publicAttemptId;
    private UUID publicExamId;
    private UUID userId;
    private LocalDateTime startTime;
}
