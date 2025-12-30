package com.project.byeoryback.domain.setting.menu.repository;

import com.project.byeoryback.domain.setting.menu.entity.MenuSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MenuSettingRepository extends JpaRepository<MenuSetting, Long> {
    Optional<MenuSetting> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
