package com.example.exam_system.configuration.security;

import com.example.exam_system.features.account.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final NimbusJwtDecoder nimbusJwtDecoder;

    public CustomJwtDecoder(@Value("${jwt.sign-key}") String SIGN_KEY, CustomJwtBlackListToken customJwtBlackListToken) {
        SecretKeySpec spec = new SecretKeySpec(SIGN_KEY.getBytes(), "HS256");
        NimbusJwtDecoder decoder =  NimbusJwtDecoder
                .withSecretKey(spec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        JwtTimestampValidator timestampValidator = new JwtTimestampValidator(Duration.ZERO);
        OAuth2TokenValidator<Jwt> jwtRule = new DelegatingOAuth2TokenValidator<>(
                timestampValidator,
                customJwtBlackListToken);

        decoder.setJwtValidator(jwtRule);
        this.nimbusJwtDecoder = decoder;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        // Chịu trách nhiệm verifier mọi request kèm token
        // Triển khai LogOut và Refresh
        return nimbusJwtDecoder.decode(token);
    }
}
