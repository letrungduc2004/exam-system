package com.example.exam_system.features.exam_attempt.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassFailResponse {
    // Phục vụ cho việc chấm điểm liệt
    private Double scorePart;
    private Double totalScore;
    private ScoreFailResponse getScoring;
    private String partName;

    // Kiểm tra pass cuối ?
    private boolean isLastPass;
    //  true: đang ở trạng thái pass (chưa bị liệt)
    // false: đã bị điểm liệt ở part trước
    private boolean isPass;
}
