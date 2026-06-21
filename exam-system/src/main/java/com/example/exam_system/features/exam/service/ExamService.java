package com.example.exam_system.features.exam.service;

import com.example.exam_system.features.exam.dto.ExamRequest;
import com.example.exam_system.features.exam.dto.ExamResponse;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.exam.mapper.ExamMapping;
import com.example.exam_system.features.exam.repository.ExamRepository;
import com.example.exam_system.features.exam.repository.projection.ExamProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final ExamMapping examMapping;

    public Page<ExamResponse> getExam(String examType, Integer difficulty,
                                      Double priceFrom, Double priceTo, Pageable pageable) {
        Page<ExamProjection> examPage = examRepository.findExam(examType, difficulty, priceFrom, priceTo, pageable);
        List<ExamResponse> examConvert = new ArrayList<>();
        for (ExamProjection examProjection : examPage.getContent()) {
            ExamResponse response = examMapping.mappingExamResponse(examProjection);
            examConvert.add(response);
        }
        return new PageImpl(examConvert, pageable, examPage.getTotalPages());
    }

    // @PreAuthorize("has")
    @Transactional(rollbackFor = Exception.class)
    public ExamResponse createExam(ExamRequest examRequest) {
        Optional<Exam> existingExam = examRepository.findByTitle(examRequest.getTitle());
        if (existingExam.isPresent()) {
            throw  new AppException(ErrorCode.EXAM_EXIST);
        }
        Exam mappingExam = examMapping.mappingExam(examRequest);
        mappingExam.setCreatedAt(LocalDateTime.now());
        mappingExam.setNumberTimes(0);
        examRepository.save(mappingExam);
        return examMapping.examExamResponse(mappingExam);
    }

    @Transactional(rollbackFor = Exception.class)
    public ExamResponse updateExam(Integer id, ExamRequest examRequest) {
        Exam exam = examRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        examMapping.mappingToExam(exam, examRequest);
        return examMapping.examExamResponse(exam);
    }

    @Transactional(rollbackFor = Exception.class)
    public ExamResponse deleteExam(Integer id) {
        Exam exam = examRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        examRepository.delete(exam);
        return examMapping.examExamResponse(exam);
    }

}
