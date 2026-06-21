package com.example.exam_system.configuration.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // COMMON
    SYSTEM_ERROR(1001, "System error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(1002, "System unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    VALIDATION_FAIL(1003, "Validation failure", HttpStatus.UNPROCESSABLE_ENTITY),
    RESOURCE_ALREADY_EXISTS(1004, "Resource already exists", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND(1005, "Resource not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1006, "Invalid request", HttpStatus.BAD_REQUEST),
    OPERATION_NOT_ALLOWED(1007, "Operation not allowed", HttpStatus.METHOD_NOT_ALLOWED),

    // SECURITY
    UNAUTHORIZED(2000, "Your login session has ended, please sign in again", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(2001, "Access denied", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(2002, "Token expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(2003, "Invalid token",HttpStatus.UNAUTHORIZED),

    PERMISSION_EXIST(2004, "Permession name already exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_EXIST(2005, "Permession name not exists", HttpStatus.CONFLICT),
    ROLE_EXIST(2006, "Role name already exists", HttpStatus.CONFLICT),
    ROLE_NOT_EXIST(2007, "Role name not exists", HttpStatus.CONFLICT),

    // FIELD VALIDATION
    EXAM_NOT_FOUND(3000, "Exam not found", HttpStatus.NOT_FOUND),
    EXAM_EXIST(3001, "Exam already exists", HttpStatus.CONFLICT),
    EXAM_TYPE(3002, "Exam type not found", HttpStatus.NOT_FOUND),

    // EXAM_ATTEMPT
    EXAM_ATTEMPT_NOT_FOUND(3003, "Exam attempt not found", HttpStatus.NOT_FOUND),
    EXAM_ATTEMPT_SUBMIT(3004, "Your exam has bean  submit. Can not change answer !", HttpStatus.CONFLICT),


    // USER
    USER_NOT_FOUND(4000, "Your account not exists", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(4001, "User already exists", HttpStatus.CONFLICT),
    USERNAME_EMPTY(4002, "User Name can not be empty", HttpStatus.BAD_REQUEST),
    FULLNAME_EMPTY(4003, "Full Name can not be empty", HttpStatus.BAD_REQUEST),
    EMAIL_EMPTY(4004, "Enail can not be empty", HttpStatus.BAD_REQUEST),
    EMAIL_ISVALID(4005, "Email is not correct format(abc@gmail.com)", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(4006, "Password can not be empty", HttpStatus.BAD_REQUEST),
    PASSWORD_ISVALID(4007, "Password must be 8 character include characters and numbers", HttpStatus.BAD_REQUEST),
    ;
    private int code;
    private String message;
    private HttpStatus httpStatus;
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
