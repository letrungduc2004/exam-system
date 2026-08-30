package com.example.exam_system.features.exam.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDetailResponse {
    private UUID publicExamId;
    private String title;
    private String examType;
    private Integer duration;
    private String level;
    private String difficulty;
    private String price;
    private String discription;
    private Double start;
    private String userCount;
    private Long totalQuestion;
    private Double maxScore;

    private List<PartDetailResponse> parts;
    private List<ExamResponse> relatedExams;
    private List<ExamCommentResponse> comment;
}

