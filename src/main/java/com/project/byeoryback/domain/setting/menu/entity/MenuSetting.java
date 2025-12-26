package com.project.byeoryback.domain.setting.menu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_setting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(columnDefinition = "json", nullable = false)
    private String menuConfig;

    @Builder
    public MenuSetting(Long userId, String menuConfig) {
        this.userId = userId;
        this.menuConfig = menuConfig;
    }

    public void updateConfig(String menuConfig) {
        this.menuConfig = menuConfig;
    }
}
