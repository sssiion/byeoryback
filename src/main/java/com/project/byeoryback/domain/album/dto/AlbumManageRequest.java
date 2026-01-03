package com.project.byeoryback.domain.album.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumManageRequest {
    private String action; // "ADD", "REMOVE", "MOVE"
    private Long contentId;
    private String type; // "POST"
    private Long sourceAlbumId; // Required for "MOVE" action
}
