package com.example.exam_system.configuration.security;

import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.repository.InvalidateTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtBlackListToken implements OAuth2TokenValidator<Jwt> {
    // OAuth2TokenValidator cho phép kiểm tra thêm luật của token
    // Kiểm tra (expiration): hạn token - có sẵn
    // Kiểm tra (issuer): người cấp phép - có sẵn
    // Kiểm tra (JWT ID): mã định dang mỗi token - tự custom
    private final InvalidateTokenRepository tokenRepository;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String jwtId = token.getId();
        ErrorCode code = ErrorCode.UNAUTHORIZED;
        if(jwtId == null){
            OAuth2Error error = new OAuth2Error("missing_jti",
                    "Token thiếu JWT ID", null);
            return OAuth2TokenValidatorResult.failure(error);
        }
        // Kiểm tra token có nằm trong blacklist không
        if (jwtId != null && tokenRepository.existsById(jwtId)) {
            OAuth2Error oAuth2Error = new OAuth2Error(
                    "Invalidate Token ",
                    code.getMessage(),
                    null
                    );
            return OAuth2TokenValidatorResult.failure(oAuth2Error);
        }
        return OAuth2TokenValidatorResult.success();
    }
}
