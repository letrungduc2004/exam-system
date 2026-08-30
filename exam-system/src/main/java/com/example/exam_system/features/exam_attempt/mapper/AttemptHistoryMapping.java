package com.example.exam_system.features.exam_attempt.mapper;

import com.example.exam_system.common.enums.EXAMTYPE;
import com.example.exam_system.features.exam.dto.response.ExamHistoryResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptDetailResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptHistoryResponse;
import com.example.exam_system.features.exam_attempt.entity.ExamAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttemptHistoryMapping {
    @Mapping(target = "publicAttemptId", source = "publicId")
    @Mapping(target = "examTitle", source = "exam.title")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "definedByStartTime")
    @Mapping(target = "totalScore", expression = "java(definedTotalScore(attempt))")
    @Mapping(target = "totalTime", expression = "java(definedSubmitTime(attempt))")
    AttemptHistoryResponse toAttemptHistory(ExamAttempt attempt);
    List<AttemptHistoryResponse> toAttemptListHistory(List<ExamAttempt> attempt);


    @Named("definedByStartTime")
    default String definedByStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return startTime.format(formatter);
    }

    @Named("definedTotalScore")
    default String definedTotalScore(ExamAttempt attempt) {
        StringBuilder builder = new StringBuilder();
        if ((attempt.getExam().getExamType().equals(EXAMTYPE.JLPT.name()))) {
            return String.valueOf(attempt.getTotalScore()) + "/" + 180;
        }
        return String.valueOf(attempt.getTotalScore()) + "/" + 800;

    }

    @Named("definedSubmitTime")
    default String definedSubmitTime(ExamAttempt attempt) {
        LocalDateTime startTime = attempt.getStartTime();
        LocalDateTime submitTime = attempt.getSubmitTime();
        if (startTime == null || submitTime == null) {
            return "N/A";
        }
        long minutes = Duration.between(startTime, submitTime).toMinutes();
        return String.valueOf(minutes) + " phút";
    }
}
