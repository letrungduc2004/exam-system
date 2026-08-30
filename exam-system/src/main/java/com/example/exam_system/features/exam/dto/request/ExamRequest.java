package com.example.exam_system.features.exam.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRequest {
    @NotBlank(message = "EXAM_TITLE_EMPTY")
    private String title;

    @NotBlank(message = "EXAM_TYPE_EMPTY")
    private String examType;

    @NotNull(message = "EXAM_DURATION_EMPTY")
    @Positive(message = "EXAM_DURATION_POSITIVE")
    private Integer duration;

    @NotNull(message = "EXAM_LEVEL_EMPTY")
    private String level;

    @NotNull(message = "EXAM_DIFFICULTY_EMPTY")
    private Integer difficulty;

    @NotNull(message = "EXAM_PRICE_EMPTY")
    @Positive(message = "EXAM_PRICE_POSITIVE")
    private Double price;

    @NotNull(message = "EXAM_TIME_EMPTY")
    @Positive(message = "EXAM_TIME_POSITIVE")
    private Integer numberTimes;

    private String discription;

    @NotNull(message = "EXAM_TOTAL_QUESTION_EMPTY")
    @Positive(message = "EXAM_TOTAL_QUESTION_POSITIVE")
    private Long totalQuestion;
}
