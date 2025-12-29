package com.project.byeoryback.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetConfirmRequestDto {
    private String email;
    private String password;
    private String resetToken;
}
