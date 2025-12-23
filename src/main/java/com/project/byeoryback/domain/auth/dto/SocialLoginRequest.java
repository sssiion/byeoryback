package com.project.byeoryback.domain.auth.dto;

import com.project.byeoryback.domain.user.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    private String email;
    private AuthProvider provider;
    private String providerId;
}
