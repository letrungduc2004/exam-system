package com.example.exam_system.features.exam.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ExamCommentResponse {
    private String userName;
    private String comment;
    private Integer ratingCount;

    public ExamCommentResponse(String userName, String comment, Integer ratingCount) {
        this.userName = userName;
        this.comment = comment;
        this.ratingCount = ratingCount;
    }
}
