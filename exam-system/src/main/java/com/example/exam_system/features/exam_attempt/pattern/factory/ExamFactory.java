package com.example.exam_system.features.exam_attempt.pattern.factory;


import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.exam_attempt.pattern.strategy.ExamStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ExamFactory {
    private final Map<String, ExamStrategy> examInjection = new HashMap<>();

    public ExamFactory(List<ExamStrategy> strategyType) {
        for (ExamStrategy strategy : strategyType) {
            this.examInjection.put(strategy.examType(), strategy);
        }
    }

    public ExamStrategy strategyType(String examType) {
        ExamStrategy examStrategy = examInjection.get(examType);
        if (Objects.isNull(examStrategy)) {
            throw new AppException(ErrorCode.EXAM_TYPE_STRATEGY);
        }
        return examStrategy;
    }
}
