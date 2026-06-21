package com.example.exam_system.features.authentication.controller;


import com.example.exam_system.features.authentication.dto.request.AuthenticationRequest;
import com.example.exam_system.features.authentication.dto.request.LogoutRequest;
import com.example.exam_system.features.account.dto.UserRequest;
import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.features.authentication.dto.response.AuthenticationResponse;
import com.example.exam_system.features.account.dto.UserResponse;
import com.example.exam_system.features.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
     private final AuthenticationService authenticationService ;

    @PostMapping("/login")
    public APIResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        APIResponse<AuthenticationResponse> response = APIResponse.<AuthenticationResponse>builder()
                .code(200)
                .data(authenticationService.login(request))
                .build();
        return response;
    }

    @PostMapping("/register")
    public APIResponse<UserResponse> register(@RequestBody @Valid  UserRequest userRequest) {
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
                .code(200)
                .message("Create Account Success")
                .data(authenticationService.register(userRequest))
                .build();
        return response;
    }

    @PostMapping("/log-out")
    public APIResponse<Void> logOut(@RequestBody LogoutRequest request) {
        authenticationService.logOut(request);
        APIResponse<Void> response = APIResponse.<Void>builder()
                .code(200)
                .message("You have bean Log Out on the System")
                .build();
        return response;
    }

    @PostMapping("/refresh-token")
    public APIResponse<AuthenticationResponse> refreshToken(@RequestBody LogoutRequest request) {
        APIResponse<AuthenticationResponse> response = APIResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("The token have bean create")
                .data(authenticationService.refreshToken(request))
                .build();
        return response;
    }

}
