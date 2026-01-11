package com.project.byeoryback.domain.setting.header.repository;

import com.project.byeoryback.domain.setting.header.entity.HeaderSetting;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeaderSettingRepository extends JpaRepository<HeaderSetting, Long> {
    Optional<HeaderSetting> findByUser(User user);

    void deleteByUser(User user);
}
