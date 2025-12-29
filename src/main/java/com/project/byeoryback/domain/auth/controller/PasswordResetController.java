package com.project.byeoryback.domain.auth.controller;

import com.project.byeoryback.domain.auth.dto.PasswordResetConfirmRequestDto;
import com.project.byeoryback.domain.auth.dto.PasswordResetRequestDto;
import com.project.byeoryback.domain.auth.dto.PasswordResetVerifyRequestDto;
import com.project.byeoryback.domain.auth.dto.PasswordResetVerifyResponseDto;
import com.project.byeoryback.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final AuthService authService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestReset(@RequestBody PasswordResetRequestDto request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<PasswordResetVerifyResponseDto> verifyCode(
            @RequestBody PasswordResetVerifyRequestDto request) {
        return ResponseEntity.ok(authService.verifyPasswordReset(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmReset(@RequestBody PasswordResetConfirmRequestDto request) {
        authService.confirmPasswordReset(request);
        return ResponseEntity.ok().build();
    }
}
