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
import com.example.exam_system.features.account.entity.Role;
import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.mapper.AuthenticationMapping;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Value("${jwt.sign-key}")
    private String SIGN_KEY;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationMapping authenticationMapping;
    private final PasswordEncoder passwordEncoder;

    public IntrospectResponse verifyToken(IntrospectRequest request) {
        String token = request.getToken();
        try {
            //  Tạo một đối tượng để kiểm tra chữ ký JWT bằng HMAC
            JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY.getBytes());
            // Tách token thành 3 phần: header, payload, signature
            SignedJWT signedJWT = SignedJWT.parse(token);
            // So sánh: signature mới(header + payload + key) = sign cũ(signedJWT)
            boolean verifier = signedJWT.verify(jwsVerifier);
            Date date = signedJWT.getJWTClaimsSet().getExpirationTime();
            return IntrospectResponse.builder()
                    .authenticated(new Date().before(date) && verifier)
                    .build();

        } catch (Exception e) {
            log.error("can not verifer token " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

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
                .token(token(user.getUserName()))
                .authenticated(password)
                .build();
    }


    // Tạo Token: header: chứa thuật toán kí(HS256)
    //            payload: chứa thông tin (người dùng, thời gian tạo - hạn token)
    //            signature: chữ kí số tạo từ header + payload + secret key
    private String token(String userName) {
        long expirationTime = 1000 * 60 * 60;
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer("exam-system.com")
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                .claim("authority", "student")
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
}
