package com.example.exam_system.features.exam.service;

import com.example.exam_system.features.exam.entity.Explanation;
import com.example.exam_system.features.exam.repository.ExplanationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExplanationService {
    private final ExplanationRepository explanationRepository;

    public List<Explanation> getExplaining(List<Long> questionIds){
        return explanationRepository.findByQuestionIdIn(questionIds);
    }
}
