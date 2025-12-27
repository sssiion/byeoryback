package com.project.byeoryback.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.byeoryback.domain.user.enums.AuthProvider;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column // password can be null for OAuth2 users
    private String password;

    @Column(nullable = false)
    private String role; // e.g., "ROLE_USER"

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Column(nullable = false)
    @Builder.Default
    private boolean fullProfile = false;

    public void completeProfile() {
        this.fullProfile = true;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
