package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptHistoryResponse {
   private UUID publicAttemptId;
   private String examTitle;
   private String startTime;
   private String totalTime;
   private String totalScore;
   private Boolean isPassed;
}
