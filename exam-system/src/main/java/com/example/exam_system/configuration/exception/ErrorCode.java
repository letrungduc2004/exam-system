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
    UNAUTHORIZED(2000, "phiên đăng nhập của bạn đã hết hạn", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(2001, "bạn không có quyền truy cập(access denied)", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(2002, "token đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(2003, "Invalid token",HttpStatus.UNAUTHORIZED),
    TOKEN_EMPTY(2003, "token không được để trống",HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_VALID(2004, "mật khẩu hoặc tài khoản không khớp",HttpStatus.UNAUTHORIZED),


    PERMISSION_EXIST(2004, "Permession name already exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_EXIST(2005, "Permession name not exists", HttpStatus.NOT_FOUND),
    ROLE_EXIST(2006, "Role name already exists", HttpStatus.CONFLICT),
    ROLE_NOT_EXIST(2007, "Role name not exists", HttpStatus.NOT_FOUND),

    // FIELD VALIDATION
    // EXAM
    EXAM_NOT_FOUND(3000, "Exam not found", HttpStatus.NOT_FOUND),
    EXAM_EXIST(3001, "Exam already exists", HttpStatus.CONFLICT),
    EXAM_TYPE(3002, "Exam type not found", HttpStatus.NOT_FOUND),
    EXAM_TYPE_STRATEGY(3002, "Can not find exam type in system", HttpStatus.NOT_FOUND),


    EXAM_TITLE_EMPTY(3002, "Exam title can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_TYPE_EMPTY(3002, "Exam type can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_DURATION_EMPTY(3002, "Exam duration can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_DURATION_POSITIVE(3002, "Exam duration must be large than 0", HttpStatus.BAD_REQUEST),
    EXAM_LEVEL_EMPTY(3002, "Exam level can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_DIFFICULTY_EMPTY(3002, "Exam difficulty level can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_PRICE_EMPTY(3002, "Exam price can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_PRICE_POSITIVE(3002, "Exam price must be large than 0", HttpStatus.BAD_REQUEST),
    EXAM_TIME_EMPTY(3002, "Number times can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_TIME_POSITIVE(3002, "Number times must be large than 0", HttpStatus.BAD_REQUEST),
    EXAM_TOTAL_QUESTION_EMPTY(3002, "Total question can not be empty", HttpStatus.BAD_REQUEST),
    EXAM_TOTAL_QUESTION_POSITIVE(3002, "Total question must be large than 0", HttpStatus.BAD_REQUEST),

    // EXAM_ATTEMPT
    EXAM_ATTEMPT_NOT_FOUND(3003, "Exam attempt not found", HttpStatus.NOT_FOUND),
    EXAM_ATTEMPT_SUBMIT(3004, "Your exam has bean submit. Can not change answer !", HttpStatus.CONFLICT),

    // QUESTION
    QUESTION_NOT_VALID(3005, "Question not in the part exam. Can not cheating !!!", HttpStatus.CONFLICT),

    // USER
    USER_NOT_FOUND(4000, "Your account not exists", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(4001, "User already exists", HttpStatus.CONFLICT),
    USERNAME_EMPTY(4002, "User Name can not be empty", HttpStatus.BAD_REQUEST),
    FULLNAME_EMPTY(4003, "Full Name can not be empty", HttpStatus.BAD_REQUEST),
    EMAIL_EMPTY(4004, "Email can not be empty", HttpStatus.BAD_REQUEST),
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
