package com.example.exam_system.features.exam.repository.projection;
import com.example.exam_system.common.enums.ExamType;


public interface ExamProjection {
    Long getId();
    String getTitle();
    ExamType getExamType();
    Integer getDuration();
    Integer getDifficulty();
    Integer getNumberTimes();
    Double getPrice();
}
