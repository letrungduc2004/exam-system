package com.example.exam_system.features.account.service;

import com.example.exam_system.common.enums.ROLE;
import com.example.exam_system.configuration.exception.AppException;
import com.example.exam_system.configuration.exception.ErrorCode;
import com.example.exam_system.features.account.dto.request.AuthenticationRequest;
import com.example.exam_system.features.account.dto.request.IntrospectRequest;
import com.example.exam_system.features.account.dto.request.LoginRequest;
import com.example.exam_system.features.account.dto.response.AuthenticationResponse;
import com.example.exam_system.features.account.dto.response.IntrospectResponse;
import com.example.exam_system.features.account.dto.response.LoginResponse;
import com.example.exam_system.features.account.entity.InvalidateToken;
import com.example.exam_system.features.account.entity.Permission;
import com.example.exam_system.features.account.entity.Role;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.mapper.AuthenticationMapping;
import com.example.exam_system.features.account.repository.InvalidateTokenRepository;
import com.example.exam_system.features.account.repository.RoleRepository;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final InvalidateTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationMapping authenticationMapping;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.sign-key}")
    private String SIGN_KEY;

    @Value("${jwt.time-refresh}")
    private long timeRefresh;

    @Value("${jwt.time-expiration}")
    private long expirationTime;


    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        // Kiểm tra user name và password
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean password = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!password) {
            throw new AppException(ErrorCode.PASSWORD_NOT_VALID);
        }

        // Login thành công tạo token
        return LoginResponse.builder()
                .token(token(user))
                .authenticated(password)
                .build();
    }


    // Tạo Token: header: chứa thuật toán kí(HS256)
    //            payload: chứa thông tin (người dùng, thời gian tạo - hạn token)
    //            signature: chữ kí số tạo từ header + payload + secret key
    private String token(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("exam-system.com")
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                .jwtID(UUID.randomUUID().toString())
                .claim("authority", getRoleAndPermission(user))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getRoleAndPermission(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        Set<Role> role = user.getRoles();
        for (Role r : role) {
            joiner.add("ROLE_" + r.getName());
            Set<Permission> permissions = r.getPermissions();
            for (Permission p : permissions) {
                joiner.add(p.getName());
            }
        }
        return joiner.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse register(AuthenticationRequest request) {
        boolean existingUser = userRepository.existsByUserName(request.getUserName());
        if (existingUser) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = authenticationMapping.toUser(request);
        user.setCreatedAt(new Date());
        // Set Role
        List<Role> role = roleRepository.findAllById(List.of(ROLE.STUDENT.name()));
        user.setRoles(new HashSet<>(role));

        // Set Password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return authenticationMapping.toAuthenticationResponse(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public IntrospectResponse refreshToken(IntrospectRequest request) {
        // Kiểm tra token cũ của user mà sever cấp (đã kiểm tra verifyToken)
        // Kiểm tra token trong blackList (đã kiểm tra verifyToken)
        // Kiểm tra token đã quá hạn được cấp phép refresh chưa
        String token = request.getToken();
        try {
            SignedJWT signedJWT = verifyToken(token);
            String userName = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            Instant expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            Instant maxToRefresh = expirationTime.plusMillis(timeRefresh);

            // Token cũ cho vào BlackList
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            InvalidateToken invalidate = InvalidateToken.builder()
                    .id(jwtId)
                    .expirationTime(expirationTime)
                    .build();
            tokenRepository.save(invalidate);
            // Refresh: nếu time hiện tại < (hạn token cũ + thời gian cho phép refresh)
            if (Instant.now().isAfter(maxToRefresh)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            return IntrospectResponse.builder()
                    .token(token(user))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void logoutToken(IntrospectRequest request) {
        // Kiểm tra token cũ của user mà sever cấp (đã kiểm tra verifyToken)
        // Kiểm tra token trong blackList (đã kiểm tra verifyToken)
        // Kiểm tra token đã quá hạn được cấp phép logout chưa (Không kiểm tra)
        try {
            SignedJWT signedJWT = verifyToken(request.getToken());
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            // Thêm vào blacklist
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            InvalidateToken invalidate = InvalidateToken.builder()
                    .id(jwtId)
                    .expirationTime(expirationTime.toInstant())
                    .build();
            tokenRepository.save(invalidate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isCheck = signedJWT.verify(verifier);
            if (!isCheck) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            // Kiểm tra token trong blackList
            if (jwtId == null || tokenRepository.existsById(jwtId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

    }
}
