package com.example.exam_system.configuration.security;

import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.configuration.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.error("Chạy qua request này 401");
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        APIResponse apiResponse = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        // chuyển object thành JSON để trả về cho fe
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}
