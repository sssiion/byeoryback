package com.project.byeoryback.domain.setting.theme.repository;

import com.project.byeoryback.domain.setting.theme.entity.ThemeSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeSettingRepository extends JpaRepository<ThemeSetting, Long> {
    // 사용자 ID로 설정 조회
    Optional<ThemeSetting> findByUserId(Long userId);
}
