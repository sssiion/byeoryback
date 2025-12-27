package com.project.byeoryback.domain.auth.controller;

import com.project.byeoryback.domain.auth.dto.JwtResponse;
import com.project.byeoryback.domain.auth.dto.LoginRequest;
import com.project.byeoryback.domain.auth.dto.SignupRequest;
import com.project.byeoryback.domain.auth.dto.SocialLoginRequest;
import com.project.byeoryback.domain.auth.service.AuthService;
import com.project.byeoryback.domain.auth.dto.EmailRequest;
import com.project.byeoryback.domain.auth.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    @PostMapping("/email/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) throws MessagingException {
        String number = mailService.sendSimpleMessage(request.getEmail());
        return ResponseEntity.ok(number);
    }

    @PostMapping("/email/check")
    public ResponseEntity<Boolean> checkEmail(
            @RequestBody com.project.byeoryback.domain.auth.dto.EmailCheckRequest request) {
        boolean isVerified = mailService.verifyEmailCode(request.getEmail(), request.getAuthNum());
        return ResponseEntity.ok(isVerified);
    }

    @PostMapping("/join")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/social-login")
    public ResponseEntity<JwtResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(authService.socialLogin(request));
    }
}
