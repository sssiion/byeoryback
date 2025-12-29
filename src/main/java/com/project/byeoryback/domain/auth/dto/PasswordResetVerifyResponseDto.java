package com.project.byeoryback.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetVerifyResponseDto {
    private String resetToken;

    public PasswordResetVerifyResponseDto(String resetToken) {
        this.resetToken = resetToken;
    }
}
