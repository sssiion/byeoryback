package com.project.byeoryback.domain.room.repository;

import com.project.byeoryback.domain.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findAllByRoomId(Long roomId);

    List<RoomMember> findAllByUserId(Long userId);

    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
}
