package com.project.byeoryback.domain.user.repository;

import com.project.byeoryback.domain.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    boolean existsByNickname(String nickname);

    java.util.List<UserProfile> findAllByUserIdIn(java.util.List<Long> userIds);
}
