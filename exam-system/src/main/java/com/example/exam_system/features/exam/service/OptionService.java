package com.example.exam_system.features.exam.service;

import com.example.exam_system.features.exam.dto.response.OptionFieldResponse;
import com.example.exam_system.features.exam.entity.Option;
import com.example.exam_system.features.exam.repository.OptionRepository;
import com.example.exam_system.features.exam.repository.projection.QuestionProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionRepository;

    public Option getReferenceOption(Long optionId) {
        return optionRepository.getReferenceById(optionId);
    }

    public   List<OptionFieldResponse> getOptionByQuestionId(List<Long> questionId){
        return optionRepository.getOptionByQuestionId(questionId);
    }

    public List<QuestionProjection> getQuestionByPartId(Long partId){
        return optionRepository.getQuestionByPartId(partId);
    }

    public List<QuestionProjection> getQuestionByExamId(Long examId){
        return optionRepository.getQuestionByExamId(examId);
    }
}
