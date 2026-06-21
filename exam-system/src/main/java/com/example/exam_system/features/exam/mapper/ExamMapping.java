package com.example.exam_system.features.exam.mapper;

import com.example.exam_system.features.exam.dto.ExamRequest;
import com.example.exam_system.features.exam.dto.ExamResponse;
import com.example.exam_system.features.exam.entity.Exam;
import com.example.exam_system.features.exam.repository.projection.ExamProjection;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamMapping {

    // Mapping Exam => ExamResponse
    ExamResponse examExamResponse(Exam exam);

    // Mapping ExamRequest => Exam
    Exam mappingExam(ExamRequest examRequest);
    void mappingToExam(@MappingTarget Exam exam, ExamRequest examRequest);

    // Mapping ExamProjection => ExamResponse
    @Mapping(target = "difficulty", source = "difficulty", qualifiedByName = "difficultConvert")
    ExamResponse mappingExamResponse(ExamProjection examProjection);

    @Named("difficultConvert")
    default String difficultConvert(Integer difficulty) {
        if (difficulty == null) return null;
        switch (difficulty) {
            case 1: return "Dễ";
            case 2: return "Trung Bình";
            case 3: return "Khó";
            default: return null;
        }
    }
}
