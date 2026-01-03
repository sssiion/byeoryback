package com.project.byeoryback.domain.album.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumRequest {
    private String name;
    private String tag; // Representative hashtag name
    private Long parentId;
    private Boolean isFavorite;
}
