package com.project.byeoryback.domain.room.dto;

import com.project.byeoryback.domain.room.entity.RoomMember;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomMemberResponse {
    private Long userId;
    private String email;
    private String role;

    public static RoomMemberResponse from(RoomMember member) {
        return RoomMemberResponse.builder()
                .userId(member.getUser().getId())
                .email(member.getUser().getEmail())
                .role(member.getRole().name())
                .build();
    }
}
