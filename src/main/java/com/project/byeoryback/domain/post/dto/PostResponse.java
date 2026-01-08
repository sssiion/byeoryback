package com.project.byeoryback.domain.post.dto;

import com.project.byeoryback.domain.post.entity.Block;
import com.project.byeoryback.domain.post.entity.FloatingItem;
import com.project.byeoryback.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponse {
        private Long id;
        private String title;
        private Long userId;
        private String userEmail;
        private String userNickname; // 선택 사항 (User 엔티티에 닉네임이 있다면)
        private List<Block> blocks;
        private List<FloatingItem> stickers;
        private List<FloatingItem> floatingTexts;
        private List<FloatingItem> floatingImages;
        private Boolean isFavorite;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private String mode;
        private java.util.Map<String, Object> titleStyles;
        private java.util.Map<String, Object> styles; // [Added] Paper Styles
        private List<String> tags;
        private List<Long> albumIds;

        public static PostResponse from(Post post) {
                return PostResponse.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .userId(post.getUser() != null ? post.getUser().getId() : null)
                                .userEmail(post.getUser() != null ? post.getUser().getEmail() : null)
                                .mode(post.getMode())
                                .titleStyles(post.getTitleStyles())
                                .styles(post.getStyles())
                                .tags(post.getPostHashtags().stream()
                                                .map(ph -> ph.getHashtag().getName())
                                                .toList())
                                .albumIds(post.getAlbumContents().stream()
                                                .map(ac -> ac.getParentAlbum() != null ? ac.getParentAlbum().getId()
                                                                : null)
                                                .filter(java.util.Objects::nonNull)
                                                .toList())
                                .blocks(post.getBlocks())
                                .stickers(post.getStickers())
                                .floatingTexts(post.getFloatingTexts())
                                .floatingImages(post.getFloatingImages())
                                .isFavorite(post.getIsFavorite())
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .build();
        }
}
