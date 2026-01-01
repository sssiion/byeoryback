package com.project.byeoryback.domain.pin.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String pinHash;

    @Column(nullable = false)
    @Builder.Default
    private int failureCount = 0;

    public void incrementFailureCount() {
        this.failureCount++;
    }

    public void resetFailureCount() {
        this.failureCount = 0;
    }

    public void updatePinHash(String pinHash) {
        this.pinHash = pinHash;
        this.failureCount = 0;
    }

    public boolean isLocked() {
        return this.failureCount >= 5;
    }
}
