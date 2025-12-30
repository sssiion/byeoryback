package com.project.byeoryback.domain.setting.theme.repository;

import com.project.byeoryback.domain.setting.theme.entity.ThemeSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ThemeSettingRepository extends JpaRepository<ThemeSetting, Long> {

    // 사용자 ID로 설정 조회
    Optional<ThemeSetting> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    @Modifying
    @Query(value = "INSERT INTO theme_setting (user_id, theme_config) VALUES (:userId, :themeConfig) " +
            "ON DUPLICATE KEY UPDATE theme_config = :themeConfig", nativeQuery = true)
    void upsertTheme(@Param("userId") Long userId, @Param("themeConfig") String themeConfig);
}
