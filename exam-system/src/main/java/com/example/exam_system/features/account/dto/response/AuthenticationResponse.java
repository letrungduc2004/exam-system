package com.example.exam_system.features.account.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private UUID id;
    private String userName;
    private String fullName;
    private String email;
    private Date createdAt;
}
