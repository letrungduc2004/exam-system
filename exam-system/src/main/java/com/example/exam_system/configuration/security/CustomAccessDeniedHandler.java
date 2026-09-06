package com.example.exam_system.configuration.security;

import com.example.exam_system.common.dto.APIResponse;
import com.example.exam_system.configuration.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("Chạy qua request này 403");
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        APIResponse apiResponse = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}
