package com.project.byeoryback.domain.room.repository;

import com.project.byeoryback.domain.room.entity.RoomCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomCycleRepository extends JpaRepository<RoomCycle, Long> {
    List<RoomCycle> findAllByRoomId(Long roomId);

    List<RoomCycle> findAllByRoomIdAndStatus(Long roomId, RoomCycle.CycleStatus status);
}
