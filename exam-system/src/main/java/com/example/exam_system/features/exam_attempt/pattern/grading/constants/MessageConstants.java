package com.example.exam_system.features.exam_attempt.pattern.grading.constants;

public class MessageConstants {
    public static final String MSG_FAILED_SECTION = "Bạn đã dính điểm liệt %s";
    public static final String MSG_FAILED_TOTAL = "Điểm sàn của bạn không đủ để pass";
    public static final String MSG_PASS = "Chúc mừng bạn đã Pass bài thi";
    public static final String MSG_SCALED_LEVEL = "Bạn thuộc level %s";
    public static String formatFailedSection(String partName) {
        return String.format(MSG_FAILED_SECTION, partName);
    }
    public static String formatScaledLevel(String level) {
        return String.format(MSG_SCALED_LEVEL, level);
    }
}
