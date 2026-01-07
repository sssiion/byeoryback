package com.project.byeoryback.domain.room.dto;

import com.project.byeoryback.domain.room.entity.RoomCycle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomCycleCreateRequest {
    private String title;
    private RoomCycle.CycleType type;
    private List<Long> targetMemberIds;
    private Integer timeLimitHours; // Optional for Diary
}
