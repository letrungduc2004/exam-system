package com.example.exam_system.features.account.controller;

import com.example.exam_system.features.account.dto.request.UserRequest;
import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.account.dto.response.UserResponse;
import com.example.exam_system.features.account.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PatchMapping("/user/{ids}")
    public APIResponse<UserResponse> updateExam(@PathVariable(name = "ids") UUID userId,
                                                @RequestBody @Valid UserRequest request) {
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
                .code(1000)
                .message("Update Success")
                .data(userService.updateUser(userId, request))
                .build();
        return response;
    }
}
