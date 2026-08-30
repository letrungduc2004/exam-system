package com.example.exam_system.features.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExamHistoryResponse {
    private UUID publicExamId;
    private String title;
    private String examType;
    private String level;
    private List<PartHistoryResponse> part;

    public ExamHistoryResponse(UUID publicExamId, String title, String examType , String level) {
        this.publicExamId = publicExamId;
        this.title = title;
        this.examType = examType;
        this.level = level;
    }
}
