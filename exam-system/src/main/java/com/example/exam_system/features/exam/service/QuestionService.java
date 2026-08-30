package com.example.exam_system.features.exam.service;

import com.example.exam_system.features.exam.dto.response.QuestionFieldResponse;
import com.example.exam_system.features.exam.entity.Question;
import com.example.exam_system.features.exam.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Long countQuestionInvalid(Long partId, List<Long> questionId) {
        return questionRepository.countQuestionInvalid(partId, questionId);
    }

    public Question getReferenceQuestion(Long questionId) {
        return questionRepository.getReferenceById(questionId);
    }

    public List<QuestionFieldResponse> getAllQuestion(List<Long> idCurrentPart) {
        return questionRepository.getQuestionByPartId(idCurrentPart);
    }
}
