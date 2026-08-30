package com.example.exam_system.features.exam_attempt.mapper;

import com.example.exam_system.features.exam_attempt.dto.response.AttemptStartResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptSubmitResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttemptExamMapping {

    @Mapping(target = "publicAttemptId", source = "publicId")
    @Mapping(target = "publicExamId", source = "exam.publicId")
    @Mapping(target = "userId", source = "user.id")
    AttemptStartResponse toExamAttemptResponse(ExamAttempt attempt);


    @Mapping(target = "correctAnswersCount", expression = "java(definedCorrectCount(attempt))")
    @Mapping(target = "examName", source = "exam.title")
    AttemptSubmitResponse toAttemptSubmitResponse(ExamAttempt attempt);

    @Named("definedCorrectCount")
    default String definedCorrectCount(ExamAttempt attempt){
        if(attempt == null || attempt.getExam() == null){
            return "0/0";
        }
        long correctQuestion = attempt.getCorrectAnswersCount();
        long totalQuestion = attempt.getExam().getTotalQuestion();
        return correctQuestion + "/" + totalQuestion;
    }

}
