package com.project.byeoryback.domain.community.dto;

import com.project.byeoryback.domain.post.entity.Block;
import com.project.byeoryback.domain.post.entity.FloatingItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CommunityDto {

        // [Response] 커뮤니티 게시글 상세 정보
        @Getter
        @Builder
        public static class Response {
                private Long communityId;
                private Long postId;
                private String title;
                private Map<String, Object> titleStyles; // [추가]
                private Map<String, Object> styles; // [Added] Paper Styles
                private String writerNickname; // 작성자 닉네임
                private Long viewCount;
                private Long likeCount;
                private Integer commentCount; // [Added]
                private Boolean isPublic;
                private Boolean isLiked; // [추가] 좋아요 여부
                private LocalDateTime createdAt;

                // [추가] 상세 콘텐츠
                private List<Block> blocks;
                private List<FloatingItem> stickers;
                private List<FloatingItem> floatingTexts;
                private List<FloatingItem> floatingImages;
                private List<String> tags; // [변경] 해시태그 목록 (tags로 통일)

                public static Response from(com.project.byeoryback.domain.post.entity.Post post,
                                com.project.byeoryback.domain.post.entity.PostStat postStat,
                                boolean isLiked) {
                        return Response.builder()
                                        .communityId(post.getId()) // communityId는 이제 postId와 동일
                                        .postId(post.getId())
                                        .title(post.getTitle())
                                        .titleStyles(post.getTitleStyles())
                                        .styles(post.getStyles())
                                        .writerNickname(post.getUser().getUserProfile() != null
                                                        ? post.getUser().getUserProfile().getNickname()
                                                        : "Unknown")
                                        .viewCount(postStat != null ? postStat.getViewCount() : 0L)
                                        .likeCount(postStat != null ? postStat.getLikeCount() : 0L)
                                        .isPublic(post.getIsPublic())
                                        .isLiked(isLiked)
                                        .createdAt(post.getCreatedAt())
                                        .commentCount(post.getMessages() != null ? post.getMessages().size() : 0) // [Added]
                                        .blocks(post.getBlocks())
                                        .stickers(post.getStickers())
                                        .floatingTexts(post.getFloatingTexts())
                                        .floatingImages(post.getFloatingImages())
                                        .tags(post.getPostHashtags().stream()
                                                        .map(ph -> ph.getHashtag().getName())
                                                        .collect(java.util.stream.Collectors.toList()))
                                        .build();
                }
        }
}