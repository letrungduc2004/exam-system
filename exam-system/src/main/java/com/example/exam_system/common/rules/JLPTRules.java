package com.example.exam_system.common.rules;

import com.example.exam_system.features.exam_attempt.dto.response.ScoreFailResponse;
import com.example.exam_system.features.exam_attempt.dto.response.ScoreRangeResponse;

public enum JLPTRules {
    N1("N1", 100.0, 19.0),
    N2("N2", 90.0, 19.0),
    N3("N3", 95.0, 19.0),
    N4("N4", 90.0, 19.0),
    N5("N5", 80.0, 19.0);;

    private final String level;
    private final Double passScore;
    private final Double failScore;

    JLPTRules(String level, Double passScore, Double failScore) {
        this.level = level;
        this.passScore = passScore;
        this.failScore = failScore;
    }

    public static ScoreFailResponse getScoring(String level) {
        JLPTRules rules = JLPTRules.valueOf(level);
        ScoreFailResponse score = ScoreFailResponse.builder()
                .level(rules.level)
                .failScore(rules.failScore)
                .passScore(rules.passScore)
                .build();
        return score;
    }

    public static double getFailScore(String level) {
        return JLPTRules.valueOf(level).failScore;
    }

    public static double getPassScore(String level) {
        return JLPTRules.valueOf(level).passScore;
    }
}
