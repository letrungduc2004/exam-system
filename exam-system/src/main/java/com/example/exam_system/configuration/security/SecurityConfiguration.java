package com.example.exam_system.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final CustomJwtDecoder customJwtDecoder;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final String[] PUBLIC_ENDPOINT = {"/register", "/login", "/refresh-token", "/logout-token"};


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Các lỗi ở đây chỉ bắt được ở tầng Security FilterChain
        // Ngoài phạm vi(PreAuthorize, PostAuthorize sẽ không băts được)

        // Cấu hình cho phép request đi qua các endpint cố định
        http.authorizeHttpRequests(config ->
                config.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                        .anyRequest().authenticated()
        );

        // Cho phép request khác đi qua kèm token
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(config -> config.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(converter()))
                        // Bắt lỗi 401: hết hạn token, chưa đăng nhập
                        .authenticationEntryPoint(authenticationEntryPoint));

        // Bắt lỗi 403: không có quyền truy cập api
        http.exceptionHandling(config ->
                config.accessDeniedHandler(accessDeniedHandler));

        http.csrf(config -> config.disable());
        return http.build();
    }

//    @Bean
//    public JwtDecoder decoder() {
//        // Đăng kí JWT Decoder kiểm tra token hợp lệ thay vì viết hàm verifyToken
//        SecretKeySpec spec = new SecretKeySpec(SIGN_KEY.getBytes(), "HS256");
//        NimbusJwtDecoder decoder = NimbusJwtDecoder
//                .withSecretKey(spec) // Đăng ký secret key để kiểm tra chữ ký JWT
//                .macAlgorithm(MacAlgorithm.HS256)
//                .build();
//        return decoder;
//    }

    @Bean
    public JwtAuthenticationConverter converter() {
        JwtGrantedAuthoritiesConverter grant = new JwtGrantedAuthoritiesConverter();
        grant.setAuthoritiesClaimName("authority");
        grant.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grant);
        return converter;
    }
}
