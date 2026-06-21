package com.example.exam_system.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final String[] PUBLIC_ENDPOINT = {"/login", "/register", "/log-out", "/refresh-token"};
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final  CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(config ->
                config.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(config -> config
                .jwt(jwtConfig -> jwtConfig.decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(converter()))
                .authenticationEntryPoint(authenticationEntryPoint)
        );

        httpSecurity.exceptionHandling(config ->
                config.accessDeniedHandler(accessDeniedHandler));

        httpSecurity.csrf(config -> config.disable());
        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter converter() {
        JwtGrantedAuthoritiesConverter granted = new JwtGrantedAuthoritiesConverter();
        granted.setAuthorityPrefix("");
        JwtAuthenticationConverter conveter = new JwtAuthenticationConverter();
        conveter.setJwtGrantedAuthoritiesConverter(granted);
        return conveter;
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signKey.getBytes(), "HS256");
//        NimbusJwtDecoder decoder = NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS256)
//                .build();
//        return decoder;
//    }

}
