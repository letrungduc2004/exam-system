package com.example.exam_system.features.exam.controller;

import com.example.exam_system.features.exam.dto.ExamRequest;
import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.exam.dto.ExamResponse;
import com.example.exam_system.features.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/exams")
    public APIResponse<Page<ExamResponse>> getExam(@RequestParam(name = "examType", required = false) String examType,
                                                   @RequestParam(name = "difficulty", required = false) Integer difficulty,
                                                   @RequestParam(name = "priceMin", required = false) Double priceFrom,
                                                   @RequestParam(name = "priceMax", required = false) Double priceTo,
                                                   @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        APIResponse<Page<ExamResponse>> response = APIResponse.<Page<ExamResponse>>builder()
                .code(200)
                .message("Find Success")
                .data(examService.getExam(examType, difficulty, priceFrom, priceTo, pageable))
                .build();
        return response;
    }

    @PostMapping("/exams")
    public APIResponse<ExamResponse> createExam(@RequestBody ExamRequest examRequest) {
        APIResponse<ExamResponse> response = APIResponse.<ExamResponse>builder()
                .code(200)
                .message("Create Success")
                .data(examService.createExam(examRequest))
                .build();
        return response;
    }

    @PutMapping("/exams/{ids}")
    public APIResponse<ExamResponse> updateExam(@PathVariable("ids") Integer ids,
                                                @RequestBody ExamRequest examRequest) {
        APIResponse<ExamResponse> response = APIResponse.<ExamResponse>builder()
                .code(200)
                .message("Update Success")
                .data(examService.updateExam(ids, examRequest))
                .build();
        return response;
    }

    @DeleteMapping("/exams/{ids}")
    public APIResponse<ExamResponse> deleteExam(@PathVariable("ids") Integer id) {
        ExamResponse exam = examService.deleteExam(id);
        APIResponse<ExamResponse> response = APIResponse.<ExamResponse>builder()
                .code(200)
                .message("Delete Success")
                .build();
        return response;
    }



}
