package com.example.exam_system.features.exam_attempt.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptStartRequest {
    private UUID userId;
    private UUID publicExamId;
}
