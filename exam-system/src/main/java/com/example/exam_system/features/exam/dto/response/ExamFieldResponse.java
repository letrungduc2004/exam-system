package com.example.exam_system.features.exam.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ExamFieldResponse {
    // lấy ra câu hỏi cho bài thi
    private UUID publicExamId;
    private String title;
    private String examType;
    private Integer duration;
    private String level;
    private boolean isMultiPart;
    private List<PartFieldResponse> partList;

    public ExamFieldResponse(UUID publicExamId, String title, String examType, Integer duration, String level) {
        this.publicExamId = publicExamId;
        this.title = title;
        this.examType = examType;
        this.duration = duration;
        this.level = level;
    }
}
