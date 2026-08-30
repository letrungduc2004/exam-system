package com.example.exam_system.features.exam_attempt.pattern.grading.impl;

import com.example.exam_system.features.exam_attempt.dto.response.GradingResultResponse;
import com.example.exam_system.features.exam_attempt.dto.response.PassFailResponse;
import com.example.exam_system.features.exam_attempt.pattern.grading.GradingStrategy;
import com.example.exam_system.features.exam_attempt.pattern.grading.constants.MessageConstants;
import org.springframework.stereotype.Component;

@Component("PassFail")
public class PassFailGradingImpl implements GradingStrategy<PassFailResponse> {
    // Xây dựng Logic Chấm xét điểm liệt (JLPT)

    // Tư tưởng: Khi người dùng nộp 1 part, hệ thống kiểm tra
    // (Part 1 - 3) Điểm liệt ? Lưu DB : isPassed(false)
    // Note: khi điểm liệt được lưu thì các part kế tiếp thì không update ghi đè(isPassed, message)
    // Part cuối: totalScore >= minScorePass ? (Passed) : Failed
    @Override
    public GradingResultResponse evaluate(PassFailResponse context) {
        double totalScore = context.getTotalScore();
        double partScore = context.getScorePart(); // Điểm part hiện tại
        double totalPassScore = context.getGetScoring().getPassScore(); // pass
        double failScore = context.getGetScoring().getFailScore(); // liệt

        GradingResultResponse response = new GradingResultResponse();
        response.setPassed(true);
        // Đã fail từ part nào đó
        if (!context.isPass()) {
            response.setPassed(false);
            return response;
        }

        // Kiểm tra điểm liệt
        if (partScore < failScore) {
            response.setPassed(false);
            response.setMessage(MessageConstants.formatFailedSection(context.getPartName()));
        }

        // Kiểm tra điểm total thỏa mãn không
        if (context.isLastPass() && response.isPassed()) {
            if (totalScore < totalPassScore) {
                response.setPassed(false);
                response.setMessage(MessageConstants.MSG_FAILED_TOTAL);
            } else if (totalScore >= totalPassScore) {
                //response.setPassed(true);
                response.setMessage(MessageConstants.MSG_PASS);
            }
        }
        return response;
    }
}
