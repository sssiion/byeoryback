package com.project.byeoryback.domain.setting.page.repository;

import com.project.byeoryback.domain.setting.page.entity.PageSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PageSettingRepository extends JpaRepository<PageSetting, Long> {
    Optional<PageSetting> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}