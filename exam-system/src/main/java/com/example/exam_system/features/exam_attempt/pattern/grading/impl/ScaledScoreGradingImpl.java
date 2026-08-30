package com.example.exam_system.features.exam_attempt.pattern.grading.impl;

import com.example.exam_system.features.exam_attempt.dto.response.ScoreRangeResponse;
import com.example.exam_system.features.exam_attempt.dto.response.GradingResultResponse;
import com.example.exam_system.features.exam_attempt.dto.response.ScaledScoreResponse;
import com.example.exam_system.features.exam_attempt.pattern.grading.GradingStrategy;
import com.example.exam_system.features.exam_attempt.pattern.grading.constants.MessageConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ScaleScore")
public class ScaledScoreGradingImpl implements GradingStrategy<ScaledScoreResponse> {
    // Xây dựng Logic Chấm theo Bảng quy đổi (BJT)
    // Tư tưởng: xếp hạng phân loại năng lực dựa theo điểm thi của người dùng
    // Khi người dùng nộp bài: phân loại kết quả

    @Override
    public GradingResultResponse evaluate(ScaledScoreResponse grade) {
        List<ScoreRangeResponse> scoringConvert = grade.getGetScoring();
        double scoreRaw = grade.getScoreRaw();
        StringBuilder builder = new StringBuilder();
        for (ScoreRangeResponse response : scoringConvert) {
            String level = response.getLevel();
            double minScore = response.getScoreFrom();
            double maxScore = response.getScoreTo();
            if (scoreRaw >= minScore && scoreRaw <= maxScore) {
                builder.append(MessageConstants.formatScaledLevel(level));
                break;
            }
        }
        return GradingResultResponse.builder()
                .score(scoreRaw)
                .isPassed(true)
                .message(builder.toString())
                .build();
    }
}
