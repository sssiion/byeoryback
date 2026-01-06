package com.project.byeoryback.domain.room.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomRequest {
    private String name;
    private String description;
    private String password;
    private String coverImage;
    private String tagName;
    private java.util.Map<String, Object> coverConfig;
}
