package com.example.exam_system.features.account.dto.response;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectResponse {
    private String token;
}
