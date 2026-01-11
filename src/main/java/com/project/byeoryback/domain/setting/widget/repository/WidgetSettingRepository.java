package com.project.byeoryback.domain.setting.widget.repository;

import com.project.byeoryback.domain.setting.widget.entity.WidgetSetting;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WidgetSettingRepository extends JpaRepository<WidgetSetting, Long> {
    Optional<WidgetSetting> findByUser(User user);

    void deleteByUser(User user);
}
