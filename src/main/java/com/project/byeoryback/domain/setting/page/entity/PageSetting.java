package com.project.byeoryback.domain.setting.page.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "page_setting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(columnDefinition = "json", nullable = false)
    private String pageConfig;

    @Builder
    public PageSetting(Long userId, String pageConfig) {
        this.userId = userId;
        this.pageConfig = pageConfig;
    }

    public void updateConfig(String pageConfig) {
        this.pageConfig = pageConfig;
    }
}