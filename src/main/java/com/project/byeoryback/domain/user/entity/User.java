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

    @Column(nullable = false)
    @Builder.Default
    private Long todayPlayTime = 0L; // Seconds

    private java.time.LocalDate lastPlayDate;

    public void completeProfile() {
        this.fullProfile = true;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void recordHeartbeat(java.time.LocalDate today, Long incrementSeconds) {
        if (this.lastPlayDate == null || !this.lastPlayDate.equals(today)) {
            this.todayPlayTime = 0L;
            this.lastPlayDate = today;
        }
        this.todayPlayTime += incrementSeconds;
    }

    @Column
    private String pinCode;

    @Column(nullable = false)
    @Builder.Default
    private boolean pinEnabled = false;

    @Column(nullable = false)
    @Builder.Default
    private int pinFailedAttempts = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean isPinLocked = false;

    @Column
    private String pinUnlockCode;

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
        this.pinEnabled = true; // Automatically enable on setup? Requirement says "PIN settings can be turned
                                // on/off", so maybe separate.
        // But "First time using PIN... input 4 digits and save as hash" implies
        // enablement.
        // Let's keep it manual or handle in service. For now just field setters.
    }

    public void enablePin() {
        this.pinEnabled = true;
    }

    public void disablePin() {
        this.pinEnabled = false;
        this.pinFailedAttempts = 0;
        this.isPinLocked = false;
        this.pinUnlockCode = null;
    }

    public void incrementPinFailedAttempts() {
        this.pinFailedAttempts++;
    }

    public void resetPinFailedAttempts() {
        this.pinFailedAttempts = 0;
    }

    public void lockPin(String unlockCode) {
        this.isPinLocked = true;
        this.pinUnlockCode = unlockCode;
    }

    public void unlockPin() {
        this.isPinLocked = false;
        this.pinFailedAttempts = 0;
        this.pinUnlockCode = null;
    }
}
