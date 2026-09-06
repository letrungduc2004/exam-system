package com.example.exam_system.common.rules;


import java.util.ArrayList;
import java.util.List;

public enum BJTRules {
    J5("J5", 0.0, 199.0),
    J4("J4", 200.0, 319.0),
    J3("J3", 320.0, 419.0),
    J2("J2", 420.0, 529.0),
    J1("J1", 530.0, 599.0),
    J1_PLUS("J1+", 600.0, 800.0);

    private final String level;
    private final Double minScore;
    private final Double maxScore;

    BJTRules(String level, Double minScore, Double maxScore) {
        this.level = level;
        this.minScore = minScore;
        this.maxScore = maxScore;

    }

}
