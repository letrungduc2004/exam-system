package com.example.exam_system.configuration.security;

import com.example.exam_system.features.authentication.service.AuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final String signKey;
    private final AuthenticationService service;
    private  NimbusJwtDecoder nimbusJwtDecoder;

    public CustomJwtDecoder(@Value("${jwt.sign-key}") String signKey, AuthenticationService service) {
        this.service = service;
        this.signKey = signKey;

        SecretKeySpec secretKeySpec = new SecretKeySpec(signKey.getBytes(), "HS256");
        this.nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        log.info("Mọi request có Token đều đi qua đây");
        SignedJWT signedJWT =  service.verifiedToken(token);
        return nimbusJwtDecoder.decode(token);
    }
}
