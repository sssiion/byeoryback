package com.project.byeoryback.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetVerifyRequestDto {
    private String email;
    private String code;
}
