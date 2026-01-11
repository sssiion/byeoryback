package com.project.byeoryback.domain.setting.header.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "header_settings")
public class HeaderSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private boolean showTimer = false; // 기본값: 꺼짐

    @Column(nullable = false)
    @Builder.Default
    private boolean showCredit = true; // 기본값: 켜짐
}
