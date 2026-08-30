package com.example.exam_system.features.account.dto.request;

import com.example.exam_system.configuration.exception.validator.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotEmpty(message = "USERNAME_EMPTY")
    private String userName;

    @NotEmpty(message = "PASSWORD_EMPTY")
    @PasswordValidation(message = "PASSWORD_ISVALID")
    private String password;
}
