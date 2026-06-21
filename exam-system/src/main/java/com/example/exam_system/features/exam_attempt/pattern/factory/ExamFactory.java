package com.example.exam_system.features.exam_attempt.pattern.factory;

import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ExamFactory {
    private final Map<String, ExamStrategy> injectMap = new HashMap<>();

    public ExamFactory(List<ExamStrategy> examStrategy) {
        for (ExamStrategy ex : examStrategy) {
            injectMap.put(ex.examType(), ex);
        }
    }

    public ExamStrategy getExamType(String type) {
        ExamStrategy examType = injectMap.get(type);
        if (Objects.isNull(examType)) {
            throw new AppException(ErrorCode.EXAM_TYPE);
        }
        return examType;
    }

}
