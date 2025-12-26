package com.project.byeoryback.domain.setting.theme.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "theme_setting")
public class ThemeSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    // ThemeDto 객체 Json 변환 및 삽입
    @Column(columnDefinition = "json", nullable = false)
    private String themeConfig;

    @Builder
    public ThemeSetting(Long userId, String themeConfig) {
        this.userId = userId;
        this.themeConfig = themeConfig;
    }

    public  void updateConfig(String themeConfig) {
        this.themeConfig = themeConfig;
    }
}
