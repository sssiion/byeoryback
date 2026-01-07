package com.project.byeoryback.domain.room.dto;

import com.project.byeoryback.domain.room.entity.RoomCycle;
import com.project.byeoryback.domain.room.entity.RoomCycleMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RoomCycleResponse {
    private Long id;
    private Long roomId;
    private String title;
    private RoomCycle.CycleType type;
    private RoomCycle.CycleStatus status;
    private Integer currentTurnOrder;
    private LocalDateTime nextTurnTime;
    private boolean isMyTurn;
    private Integer totalMembers;
    private Long postId;
    private List<MemberDto> members;

    public static RoomCycleResponse from(RoomCycle cycle, Long currentUserId, java.util.Map<Long, String> nicknames) {
        boolean isMyTurn = false;
        // Check if it's my turn
        if (cycle.getStatus() == RoomCycle.CycleStatus.IN_PROGRESS && cycle.getMembers() != null) {
            isMyTurn = cycle.getMembers().stream()
                    .filter(m -> m.getTurnOrder().equals(cycle.getCurrentTurnOrder()))
                    .anyMatch(m -> m.getUser().getId().equals(currentUserId));
        }

        return RoomCycleResponse.builder()
                .id(cycle.getId())
                .roomId(cycle.getRoom().getId())
                .title(cycle.getTitle())
                .type(cycle.getType())
                .status(cycle.getStatus())
                .currentTurnOrder(cycle.getCurrentTurnOrder())
                .nextTurnTime(cycle.getNextTurnTime())
                .isMyTurn(isMyTurn)
                .totalMembers(cycle.getMembers() != null ? cycle.getMembers().size() : 0)
                .postId(cycle.getPostId())
                .members(cycle.getMembers() != null ? cycle.getMembers().stream()
                        .map(m -> MemberDto.from(m,
                                nicknames.getOrDefault(m.getUser().getId(), m.getUser().getEmail().split("@")[0])))
                        .toList()
                        : List.of())
                .build();
    }

    @Getter
    @Builder
    public static class MemberDto {
        private Long userId;
        private String nickname; // To be populated properly
        private String email;
        private Integer order;
        private boolean isCompleted;

        public static MemberDto from(RoomCycleMember member, String nickname) {
            return MemberDto.builder()
                    .userId(member.getUser().getId())
                    .nickname(nickname)
                    .email(member.getUser().getEmail())
                    .order(member.getTurnOrder())
                    .isCompleted(member.isCompleted())
                    .build();
        }
    }
}
