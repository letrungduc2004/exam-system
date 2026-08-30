package com.example.exam_system.features.exam_attempt.controller;


import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.exam.dto.response.ExamFieldResponse;
import com.example.exam_system.features.exam_attempt.dto.request.AnswerRequest;
import com.example.exam_system.features.exam_attempt.dto.request.AttemptStartRequest;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptStartResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptSubmitResponse;
import com.example.exam_system.features.exam_attempt.service.ExamAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AttemptExamController {
    private final ExamAttemptService examAttemptService;

    @PostMapping("/exam-attempt")
    public APIResponse<AttemptStartResponse> startExam(@RequestBody AttemptStartRequest request) {
        APIResponse<AttemptStartResponse> apiResponse = APIResponse.<AttemptStartResponse>builder()
                .code(1000)
                .data(examAttemptService.startExam(request))
                .message("Đã khởi tạo bài thi thành công")
                .build();
        return apiResponse;
    }

    @GetMapping("/exam-attempt/question/{publicAttemptId}")
    public APIResponse<ExamFieldResponse> getQuestionForAttempt(@PathVariable(name = "publicAttemptId") UUID publicAttemptId) {
        APIResponse<ExamFieldResponse> apiResponse = APIResponse.<ExamFieldResponse>builder()
                .code(1000)
                .data(examAttemptService.getQuestion(publicAttemptId))
                .build();
        return apiResponse;
    }


    @PostMapping("/exam-attempt/auto-save/{publicAttemptId}")
    public APIResponse<Void> autoSaveExam(@PathVariable(name = "publicAttemptId") UUID publicAttemptId,
                                          @RequestBody List<AnswerRequest> studentAnswer) {
        examAttemptService.autoSaveQuestion(publicAttemptId, studentAnswer);
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .code(1000)
                .message("Bài thi đã được lưu thành công")
                .build();
        return apiResponse;
    }

    @PostMapping("/submit-exam/{publicAttemptId}")
    public APIResponse<AttemptSubmitResponse> submitExam(@PathVariable(name = "publicAttemptId") UUID publicAttemptId,
                                          @RequestBody List<AnswerRequest> studentAnswer) {
        APIResponse<AttemptSubmitResponse> apiResponse = APIResponse.<AttemptSubmitResponse>builder()
                .code(1000)
                .data(examAttemptService.submitExam(publicAttemptId, studentAnswer))
                .build();
        return apiResponse;
    }
}
