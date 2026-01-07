package com.project.byeoryback.domain.room.repository;

import com.project.byeoryback.domain.room.entity.RoomCycleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomCycleMemberRepository extends JpaRepository<RoomCycleMember, Long> {
    Optional<RoomCycleMember> findByCycleIdAndUserId(Long cycleId, Long userId);
}
