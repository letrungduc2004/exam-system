package com.example.exam_system.features.exam.repository.projection;

public interface QuestionProjection {
    Long getQuestionId();
    Long getOptionId();
    Double getScoreWeight();
}
