package com.project.byeoryback.domain.album.dto;

import com.project.byeoryback.domain.album.entity.Album;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AlbumResponse {
    private Long id;
    private String name;
    private String tag;
    private Long parentId;
    private Boolean isFavorite;
    private LocalDateTime createdAt;

    public static AlbumResponse from(Album album) {
        return AlbumResponse.builder()
                .id(album.getId())
                .name(album.getName())
                .tag(album.getRepresentativeHashtag() != null ? album.getRepresentativeHashtag().getName() : null)
                .parentId(album.getParent() != null ? album.getParent().getId() : null)
                .isFavorite(album.getIsFavorite())
                .createdAt(album.getCreatedAt())
                .build();
    }
}
