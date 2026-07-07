package com.example.exam_system.features.account.dto;

import com.example.exam_system.configuration.exception.validator.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotEmpty(message = "USERNAME_EMPTY")
    private String userName;

    @NotEmpty(message = "FULLNAME_EMPTY")
    private String fullName;

    @NotEmpty(message = "EMAIL_EMPTY")
    @Email(message = "EMAIL_ISVALID")
    private String email;

    @NotEmpty(message = "PASSWORD_EMPTY")
    @PasswordValidation(message = "PASSWORD_ISVALID")
    private String password;

    private Set<String> roles;
}
