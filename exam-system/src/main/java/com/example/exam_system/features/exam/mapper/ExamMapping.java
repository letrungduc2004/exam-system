package com.example.exam_system.features.exam.mapper;

import com.example.exam_system.features.exam.dto.request.ExamRequest;
import com.example.exam_system.features.exam.dto.response.ExamResponse;
import com.example.exam_system.features.exam.entity.Exam;
import org.mapstruct.*;

import java.text.DecimalFormat;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamMapping {

    // Mapping for create
    Exam toExam(ExamRequest exam);

    // Mapping for update
    void toUpdateExam(@MappingTarget Exam exam, ExamRequest request);

    // Mapping to get
    @Mapping(target = "publicExamId", source = "publicId")
    @Mapping(target = "difficulty", source = "difficulty", qualifiedByName = "definedDifficulty")
    @Mapping(target = "price", source = "price", qualifiedByName = "definedPrice")
    @Mapping(target = "userCount", source = "userCount", qualifiedByName = "definedUserCount")
    ExamResponse toExamResponse(Exam exam);

    List<ExamResponse> toListExamResponse(List<Exam> exams);

    @Named("definedDifficulty")
    default String definedDifficult(Integer difficult) {
        if (difficult == 1) {
            return "Dễ";
        } else if (difficult == 2) {
            return "Trung bình";
        } else {
            return "Khó";
        }
    }

    @Named("definedPrice")
    default String definedPrice(Double price) {
        if (price == null) {
            return "N/A";
        }
        String formatted = "";
        if (price != 0) {
            DecimalFormat formatter = new DecimalFormat("#,###");
            formatted = formatter.format(price);
        } else {
            formatted = "miễn phí";
        }
        return formatted;
    }

    @Named("definedUserCount")
    default String definedUserCount(Long number) {
        if (number == null) {
            return "N/A";
        }
        if (number < 1000) {
            return String.valueOf(number);
        }
        if (number < 1_000_000) {
            double value = number / 1000.0;
            if (value == (long) value) {
                return (long) value + "k";
            }
            return String.format("%.1fk", value);
        }
        return String.valueOf(number);
    }

}
