package com.example.exam_system.features.exam_attempt.pattern.grading;

import com.example.exam_system.features.exam_attempt.dto.response.GradingResultResponse;

public interface GradingStrategy<T> {
    GradingResultResponse evaluate(T context) ;
}
