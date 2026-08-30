package com.example.exam_system.features.exam.mapper;

import com.example.exam_system.features.exam.dto.response.*;
import com.example.exam_system.features.exam.entity.Exam;
import org.mapstruct.*;

import java.text.DecimalFormat;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamDetailMapping {
    @Mapping(target = "publicExamId", source = "publicId")
    @Mapping(target = "difficulty", source = "difficulty", qualifiedByName = "definedDifficulty")
    @Mapping(target = "price", source = "price", qualifiedByName = "definedPrice")
    @Mapping(target = "userCount", source = "userCount", qualifiedByName = "definedUserCount")
    @Mapping(target = "parts", ignore = true)
    @Mapping(target = "relatedExams", ignore = true)
    @Mapping(target = "comment", ignore = true)
    ExamDetailResponse toExamDetailResponse(Exam exam);


    @Mapping(target = "publicExamId", source = "publicId")
    @Mapping(target = "part", ignore = true)
    ExamHistoryResponse toExamHistoryResponse(Exam exam);

    @Mapping(target = "publicExamId", source = "publicId")
    @Mapping(target = "difficulty", source = "difficulty", qualifiedByName = "definedDifficulty")
    @Mapping(target = "price", source = "price", qualifiedByName = "definedPrice")
    @Mapping(target = "userCount", source = "userCount", qualifiedByName = "definedUserCount")
    ExamResponse toRelatedExamResponse(Exam exam);
    List<ExamResponse> toRelatedExamResponseList(List<Exam> exams);


    @Named("definedDifficulty")
    default String definedDifficulty(Integer difficult) {
        if (difficult == null) return "Không xác định";
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
