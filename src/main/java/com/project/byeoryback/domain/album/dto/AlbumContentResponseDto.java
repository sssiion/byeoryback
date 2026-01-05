package com.project.byeoryback.domain.album.dto;

import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.dto.PostResponse;
import com.project.byeoryback.domain.album.entity.Album;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumContentResponseDto {
    private Long id; // AlbumContent ID (null if auto-generated)
    private String type; // POST, ALBUM
    private Object content; // PostResponse or Album(Model)

    public static AlbumContentResponseDto fromPost(Post post) {
        return AlbumContentResponseDto.builder()
                .type("POST")
                .content(PostResponse.from(post))
                .build();
    }

    public static AlbumContentResponseDto fromAlbum(Album album, Long folderCount, Long postCount) {
        return AlbumContentResponseDto.builder()
                .type("ALBUM")
                .content(com.project.byeoryback.domain.album.dto.AlbumResponse.from(album, folderCount, postCount))
                .build();
    }
}
