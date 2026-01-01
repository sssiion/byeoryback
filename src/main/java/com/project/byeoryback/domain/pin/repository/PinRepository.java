package com.project.byeoryback.domain.pin.repository;

import com.project.byeoryback.domain.pin.entity.Pin;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {
    Optional<Pin> findByUser(User user);

    boolean existsByUser(User user);
}
