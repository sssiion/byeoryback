package com.project.byeoryback.domain.auth.service;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.enums.AuthProvider;
import com.project.byeoryback.domain.auth.dto.JwtResponse;
import com.project.byeoryback.domain.auth.dto.LoginRequest;
import com.project.byeoryback.domain.auth.dto.SignupRequest;
import com.project.byeoryback.domain.auth.dto.SocialLoginRequest;
import com.project.byeoryback.domain.user.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
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
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = jwtUtil.generateToken(user.getEmail(), user.isFullProfile());

        return JwtResponse.builder()
                .accessToken(jwt)
                .build();
    }

    public JwtResponse socialLogin(SocialLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProvider() != request.getProvider()) {
            throw new RuntimeException("Auth provider does not match");
        }

        // Optionally verify providerId matches
        if (request.getProviderId() != null && !request.getProviderId().equals(user.getProviderId())) {
            throw new RuntimeException("Invalid Provider ID");
        }

        String jwt = jwtUtil.generateToken(user.getEmail(), user.isFullProfile());

        return JwtResponse.builder()
                .accessToken(jwt)
                .build();
    }
}
