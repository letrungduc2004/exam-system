package com.example.exam_system.features.account.controller;

import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.account.dto.request.AuthenticationRequest;
import com.example.exam_system.features.account.dto.request.IntrospectRequest;
import com.example.exam_system.features.account.dto.request.LoginRequest;
import com.example.exam_system.features.account.dto.response.AuthenticationResponse;
import com.example.exam_system.features.account.dto.response.IntrospectResponse;
import com.example.exam_system.features.account.dto.response.LoginResponse;
import com.example.exam_system.features.account.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public APIResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                .code(1000)
                .message("Login Success")
                .data(authenticationService.login(request))
                .build();
        return response;
    }

    @PostMapping("/register")
    public APIResponse<AuthenticationResponse> register(@RequestBody @Valid AuthenticationRequest request) {
        APIResponse<AuthenticationResponse> response = APIResponse.<AuthenticationResponse>builder()
                .code(1000)
                .message("Create Acoount Success")
                .data(authenticationService.register(request))
                .build();
        return response;
    }

    @PostMapping("/refresh-token")
    public APIResponse<IntrospectResponse> refreshToken(@RequestBody @Valid IntrospectRequest request) {
        APIResponse<IntrospectResponse> response = APIResponse.<IntrospectResponse>builder()
                .code(1000)
                .data(authenticationService.refreshToken(request))
                .build();
        return response;
    }

    @PostMapping("/logout-token")
    public APIResponse<Void> logoutToken(@RequestBody @Valid IntrospectRequest request) {
        authenticationService.logoutToken(request);
        APIResponse<Void> response = APIResponse.<Void>builder()
                .code(1000)
                .message("Bạn đã đăng xuất")
                .build();
        return response;
    }
}
