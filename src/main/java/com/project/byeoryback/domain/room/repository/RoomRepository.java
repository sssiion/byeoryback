package com.project.byeoryback.domain.room.repository;

import com.project.byeoryback.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByOwnerId(Long ownerId);
}
