package com.project.byeoryback.domain.album.dto;

import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.album.entity.Folder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumContentResponseDto {
    private Long id; // AlbumContent ID (null if auto-generated)
    private String type; // POST, FOLDER
    private Object content; // PostDto or FolderDto

    public static AlbumContentResponseDto fromPost(Post post) {
        return AlbumContentResponseDto.builder()
                .type("POST")
                .content(post) // In real app, use PostResponseDto
                .build();
    }

    public static AlbumContentResponseDto fromFolder(Folder folder) {
        return AlbumContentResponseDto.builder()
                .type("FOLDER")
                .content(folder)
                .build();
    }
}
