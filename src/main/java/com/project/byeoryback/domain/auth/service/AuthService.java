package com.project.byeoryback.domain.auth.service;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.enums.AuthProvider;
import com.project.byeoryback.domain.auth.dto.JwtResponse;
import com.project.byeoryback.domain.auth.dto.LoginRequest;
import com.project.byeoryback.domain.auth.dto.SignupRequest;
import com.project.byeoryback.domain.auth.dto.SocialLoginRequest;
import com.project.byeoryback.domain.auth.exception.EmailAlreadyExistsException;
import com.project.byeoryback.domain.auth.exception.InvalidPasswordException;
import com.project.byeoryback.domain.auth.exception.SocialAccountException;
import com.project.byeoryback.domain.auth.dto.PasswordResetRequestDto;
import com.project.byeoryback.domain.auth.dto.PasswordResetVerifyRequestDto;
import com.project.byeoryback.domain.auth.dto.PasswordResetVerifyResponseDto;
import com.project.byeoryback.domain.auth.dto.PasswordResetConfirmRequestDto;
import com.project.byeoryback.domain.user.exception.UserNotFoundException;
import com.project.byeoryback.domain.user.repository.UserRepository;
import com.project.byeoryback.domain.setting.widget.WidgetService;
import com.project.byeoryback.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final com.project.byeoryback.domain.pin.repository.PinRepository pinRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final WidgetService widgetService;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("이미 존재하는 계정입니다.");
        }

        AuthProvider provider = request.getProvider() != null ? request.getProvider() : AuthProvider.LOCAL;
        String encodedPassword = null;

        if (provider == AuthProvider.LOCAL) {
            if (request.getPassword() == null) {
                throw new IllegalArgumentException("Password is required for local signup");
            }
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .role("ROLE_USER")
                .provider(provider)
                .providerId(request.getProviderId())
                .build();

        userRepository.save(user);

        // 기본 위젯 설정 초기화
        widgetService.initializeDefaultWidgets(user);
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String jwt = jwtUtil.generateToken(user.getEmail(), user.isFullProfile(), user.getProvider().name());
        boolean isPinSet = pinRepository.existsByUser(user);

        return JwtResponse.builder()
                .accessToken(jwt)
                .isPinSet(isPinSet)
                .build();
    }

    @Transactional
    public JwtResponse socialLogin(SocialLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            // Create new user if not exists
            user = User.builder()
                    .email(request.getEmail())
                    .role("ROLE_USER")
                    .provider(request.getProvider())
                    .providerId(request.getProviderId())
                    .fullProfile(false)
                    .build();
            userRepository.save(user);

            // 기본 위젯 설정 초기화 (신규 소셜 로그인 사용자)
            widgetService.initializeDefaultWidgets(user);
        } else {
            // Existing user verification
            if (user.getProvider() != request.getProvider()) {
                throw new SocialAccountException("이미 " + user.getProvider() + " 계정으로 가입된 이메일입니다.");
            }
            // Optional: Update providerId if needed
        }

        String jwt = jwtUtil.generateToken(user.getEmail(), user.isFullProfile(), user.getProvider().name());
        boolean isPinSet = pinRepository.existsByUser(user);

        return JwtResponse.builder()
                .accessToken(jwt)
                .isPinSet(isPinSet)
                .build();
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new IllegalArgumentException("Only local users can change password");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        // userRepository.save(user); // Transactional will handle save
    }

    public void requestPasswordReset(PasswordResetRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new SocialAccountException("Social account cannot reset password");
        }

        try {
            mailService.sendSimpleMessage(request.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email verification code", e);
        }
    }

    public PasswordResetVerifyResponseDto verifyPasswordReset(PasswordResetVerifyRequestDto request) {
        boolean verified = mailService.verifyEmailCode(request.getEmail(), request.getCode());
        if (!verified) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        return new PasswordResetVerifyResponseDto(jwtUtil.generateResetToken(request.getEmail()));
    }

    @Transactional
    public void confirmPasswordReset(PasswordResetConfirmRequestDto request) {
        if (!jwtUtil.validateToken(request.getResetToken(), request.getEmail())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        String type = jwtUtil.extractClaim(request.getResetToken(), claims -> claims.get("type", String.class));
        if (!"RESET".equals(type)) {
            throw new IllegalArgumentException("Invalid token type");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        user.updatePassword(passwordEncoder.encode(request.getPassword()));
    }
}
