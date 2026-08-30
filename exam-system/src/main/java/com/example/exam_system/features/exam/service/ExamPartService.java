package com.example.exam_system.features.exam.service;

import com.example.exam_system.features.exam.dto.response.PartFieldResponse;
import com.example.exam_system.features.exam.entity.ExamPart;
import com.example.exam_system.features.exam.repository.ExamPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamPartService {
    private final ExamPartRepository examPartRepository;

    public ExamPart getReferencePart(Long partId) {
        return examPartRepository.getReferenceById(partId);
    }

    public List<ExamPart> getAllPartExam(Long examId) {
        return examPartRepository.findByExamIdOrderByOrderIndexAsc(examId);
    }

    public List<PartFieldResponse> getAllPart(List<Long> partId) {
        return examPartRepository.getAllExamPart(partId);
    }
}
