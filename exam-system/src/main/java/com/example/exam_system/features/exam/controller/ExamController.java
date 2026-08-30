package com.example.exam_system.features.exam.controller;


import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.exam.dto.request.ExamRequest;
import com.example.exam_system.features.exam.dto.response.ExamDetailResponse;
import com.example.exam_system.features.exam.dto.response.ExamResponse;
import com.example.exam_system.features.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamController {
    private final ExamService examService;

    @GetMapping("/exams")
    public APIResponse<Page<ExamResponse>> getExam(
            @RequestParam(required = false) List<String> examType,
            @RequestParam(required = false) List<Integer> difficulty,
            @RequestParam(required = false) List<String> level,
            @RequestParam(required = false) Double priceFrom,
            @RequestParam(required = false) Double priceTo,
            @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        APIResponse<Page<ExamResponse>> result = APIResponse.<Page<ExamResponse>>builder()
                .code(1000)
                .message("Danh sách bài kiểm tra")
                .data(examService.getExam(examType, difficulty, level, priceFrom, priceTo, pageable))
                .build();
        return result;
    }

    @GetMapping("/exams/detail/{examId}")
    public APIResponse<ExamDetailResponse> getExamDetailInformation(@PathVariable(name = "examId") UUID publicExamId) {
        APIResponse<ExamDetailResponse> result = APIResponse.<ExamDetailResponse>builder()
                .code(1000)
                .message("Chi tiết bài thi")
                .data(examService.getExamDetailInformation(publicExamId))
                .build();
        return result;
    }


    @PostMapping("/exams")
    public APIResponse<ExamResponse> createExam(@RequestBody @Valid  ExamRequest request) {
        APIResponse<ExamResponse> result = APIResponse.<ExamResponse>builder()
                .code(1000)
                .data(examService.createExam(request))
                .build();
        return result;
    }

    @PatchMapping("/exams/{examId}")
    public APIResponse<ExamResponse> updateExam(@PathVariable(name = "examId") UUID publicExamId,
                                                      @RequestBody @Valid  ExamRequest request) {
        APIResponse<ExamResponse> result = APIResponse.<ExamResponse>builder()
                .code(1000)
                .message("Cập nhật bài kiểm tra")
                .data(examService.updateExam(publicExamId, request))
                .build();
        return result;
    }


    @DeleteMapping("/exams/{examId}")
    public APIResponse<ExamResponse> deleteExam(@PathVariable(name = "examId") UUID publicExamId) {
        APIResponse<ExamResponse> result = APIResponse.<ExamResponse>builder()
                .code(1000)
                .message("Xóa bài kiểm tra")
                .data(examService.deleteExam(publicExamId))
                .build();
        return result;
    }
}
