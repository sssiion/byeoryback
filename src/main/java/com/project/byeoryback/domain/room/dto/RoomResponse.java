package com.project.byeoryback.domain.room.dto;

import com.project.byeoryback.domain.room.entity.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private boolean hasPassword;
    private String coverImage;
    private String tagName;
    private Long ownerId;
    private String ownerName;
    private int memberCount;
    private java.util.Map<String, Object> coverConfig;
    private LocalDateTime createdAt;

    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .hasPassword(room.getPassword() != null && !room.getPassword().isEmpty())
                .coverImage(room.getCoverImage())
                .tagName(room.getHashtag() != null ? room.getHashtag().getName() : null)
                .ownerId(room.getOwner().getId())
                .ownerName(room.getOwner().getEmail()) // Use email for now
                .memberCount(room.getMembers().size())
                .coverConfig(room.getCoverConfig())
                .createdAt(room.getCreatedAt())
                .build();
    }
}
