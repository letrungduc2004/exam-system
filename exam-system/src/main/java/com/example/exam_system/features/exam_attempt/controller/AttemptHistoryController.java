package com.example.exam_system.features.exam_attempt.controller;

import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptDetailResponse;
import com.example.exam_system.features.exam_attempt.dto.response.AttemptHistoryResponse;
import com.example.exam_system.features.exam_attempt.service.ExamHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AttemptHistoryController {
    private final ExamHistoryService examHistoryService;

    @GetMapping("/attempt-history/{userId}")
    public APIResponse<Page<AttemptHistoryResponse>> getAttemptHistory(@PathVariable(value = "userId") UUID userId,
                              @PageableDefault(page = 0, size = 10,sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        APIResponse<Page<AttemptHistoryResponse>> response = APIResponse.<Page<AttemptHistoryResponse>>builder()
                .code(1000)
                .data(examHistoryService.getAttemptHistory(userId, pageable))
                .build();
        return response;
    }

    @GetMapping("/attempt-history/details/{attemptPublicId}")
    public APIResponse<AttemptDetailResponse> getAttemptDetailHistory(@PathVariable(value = "attemptPublicId") UUID attemptPublicId) {
        APIResponse<AttemptDetailResponse> response = APIResponse.<AttemptDetailResponse>builder()
                .code(1000)
                .data(examHistoryService.getDetailAttemptHistory(attemptPublicId))
                .build();
        return response;
    }
}
