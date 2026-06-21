package com.example.exam_system.features.authentication.service;

import com.example.exam_system.features.authentication.dto.request.AuthenticationRequest;
import com.example.exam_system.features.authentication.dto.request.LogoutRequest;
import com.example.exam_system.features.account.dto.UserRequest;
import com.example.exam_system.features.authentication.dto.response.AuthenticationResponse;
import com.example.exam_system.features.account.dto.UserResponse;
import com.example.exam_system.features.authentication.entity.InvalidateToken;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.mapper.UserMapping;
import com.example.exam_system.features.authentication.repository.InvalidateTokenRepository;
import com.example.exam_system.features.account.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.sign-key}")
    private String signKey;
    private final UserRepository userRepository;
    private final InvalidateTokenRepository invalidateTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapping userMapping;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse register(UserRequest userRequest) {
        boolean existsByUsername = userRepository.existsByUserName(userRequest.getUserName());
        boolean existsByEmail = userRepository.existsByEmail(userRequest.getEmail());
        if (existsByUsername || existsByEmail) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = userMapping.mappingUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setCreatedAt(new Date());
        userRepository.save(user);
        return userMapping.mappingUserResponse(user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository.getUserAndRolePermission(request.getUserName()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        boolean checkLogin = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!checkLogin) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        // Login success create token
        return AuthenticationResponse.builder()
                .token(createToken(user))
                .authentication(checkLogin)
                .build();
    }

    private String createToken(User user) {
        long expirationTime = 1000 * 60 * 60;
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("exam-system.com")
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", getRoleAndPermission(user))
                .build();
        Payload payoad = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payoad);
        try {
            jwsObject.sign(new MACSigner(signKey));
            return jwsObject.serialize();
        } catch (Exception e) {
            log.error("can not create token" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRoleAndPermission(User user) {
        log.info("Start QUERY");
        StringJoiner joinner = new StringJoiner(" ");
        user.getRoles().forEach(role -> {
            joinner.add("ROLE_" + role.getName());
            if (!role.getPermissions().isEmpty()) {
                role.getPermissions().forEach(permission -> {
                    joinner.add(permission.getName());
                });
            }
        });
        return joinner.toString();
    }

    public SignedJWT verifiedToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(signKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            String existingIdInvalidate = signedJWT.getJWTClaimsSet().getJWTID();
            if (invalidateTokenRepository.existsById(existingIdInvalidate)) {
                // Token nằm trong black list
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            boolean signatureChecking = signedJWT.verify(verifier);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (!signatureChecking || !expirationTime.after(new Date())) {
                // chữ kí không khớp, token hết hạn
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            return signedJWT;
        } catch (ParseException | JOSEException e) {
            log.info("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void logOut(LogoutRequest request) {
        // kiểm tra token do hệ thống cấp hay không
        SignedJWT verified = verifiedToken(request.getToken());
        try {
            String idToken = verified.getJWTClaimsSet().getJWTID();
            Instant expirationTime = verified.getJWTClaimsSet().getExpirationTime().toInstant();

            InvalidateToken invalidate = InvalidateToken.builder()
                    .id(idToken)
                    .expirationTime(expirationTime)
                    .build();
            // Lưu Token mà người dùng muốn log out vào DB
            invalidateTokenRepository.save(invalidate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Refresh Token
    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse refreshToken(LogoutRequest request) {
        // kiểm tra token do hệ thống cấp hay không
        SignedJWT verified = verifiedToken(request.getToken());
        try {
            // Đẩy token cũ vào hệ thống
            String idToken = verified.getJWTClaimsSet().getJWTID();
            Instant expirationTime = verified.getJWTClaimsSet().getExpirationTime().toInstant();

            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .id(idToken)
                    .expirationTime(expirationTime)
                    .build();
            invalidateTokenRepository.save(invalidateToken);

            // Kiểm tra Tên người dùng tồn tại trong DB chưa
            String userName = verified.getJWTClaimsSet().getSubject();
            User user = userRepository.getUserAndRolePermission(userName).orElseThrow(() ->
                    new AppException(ErrorCode.USER_NOT_FOUND));
            String newToken = createToken(user);
            return AuthenticationResponse.builder()
                    .token(newToken)
                    .authentication(true)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
