package com.project.byeoryback.domain.post.dto;

import com.project.byeoryback.domain.post.entity.Block;
import com.project.byeoryback.domain.post.entity.FloatingItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostRequest {
    private String title;
    private List<Block> blocks;
    private List<FloatingItem> stickers;
    private List<FloatingItem> floatingTexts; // 프론트엔드 필드명과 일치해야 함
    private List<FloatingItem> floatingImages; // 프론트엔드 필드명과 일치해야 함

    private List<String> hashtags;
    private String mode; // "AUTO", "MANUAL"
    private List<Long> targetAlbumIds;
    private List<Long> targetFolderIds;
    private Boolean isFavorite;
}