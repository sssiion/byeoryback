package com.project.byeoryback.domain.post.dto;

import com.project.byeoryback.domain.post.entity.FloatingItem;
import com.project.byeoryback.domain.post.entity.PostTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PostTemplateDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String name;
        private Map<String, Object> styles;
        private List<FloatingItem> stickers;
        private List<FloatingItem> floatingTexts;
        private List<FloatingItem> floatingImages;
        private String defaultFontColor;
        private String thumbnailUrl;
        private List<String> tags;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long userId;
        private String name;
        private Map<String, Object> styles;
        private List<FloatingItem> stickers;
        private List<FloatingItem> floatingTexts;
        private List<FloatingItem> floatingImages;
        private String defaultFontColor;
        private String thumbnailUrl;
        private List<String> tags;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response from(PostTemplate entity) {
            return Response.builder()
                    .id(entity.getId())
                    .userId(entity.getUser().getId())
                    .name(entity.getName())
                    .styles(entity.getStyles())
                    .stickers(entity.getStickers())
                    .floatingTexts(entity.getFloatingTexts())
                    .floatingImages(entity.getFloatingImages())
                    .defaultFontColor(entity.getDefaultFontColor())
                    .thumbnailUrl(entity.getThumbnailUrl())
                    .tags(entity.getTags())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        }
    }
}
