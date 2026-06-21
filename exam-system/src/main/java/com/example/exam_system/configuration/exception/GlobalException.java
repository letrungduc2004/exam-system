package com.example.exam_system.configuration.exception;

import com.example.exam_system.common.dto.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIResponse> exception(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorCode error = ErrorCode.SYSTEM_ERROR;
        APIResponse response = APIResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();
        return ResponseEntity.status(error.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<APIResponse> appException(AppException ap) {
        ErrorCode errorCode = ap.getErrorCode();
        APIResponse response = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> dtoException(MethodArgumentNotValidException exception) {
        String keyException = exception.getFieldError().getDefaultMessage();
        log.error(keyException);

        ErrorCode errorCode = ErrorCode.valueOf(keyException);

        APIResponse response = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

}
