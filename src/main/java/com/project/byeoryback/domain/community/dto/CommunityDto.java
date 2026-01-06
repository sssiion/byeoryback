package com.project.byeoryback.domain.community.dto;

import com.project.byeoryback.domain.community.entity.Community;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommunityDto {

    // [Response] 커뮤니티 게시글 상세 정보
    @Getter
    @Builder
    public static class Response {
        private Long communityId;
        private Long postId;
        private String title;
        private String writerNickname; // 작성자 닉네임
        private Long viewCount;
        private Long likeCount;
        private Boolean isPublic;
        private Boolean isLiked; // [추가] 좋아요 여부
        private LocalDateTime createdAt;
        // 필요하다면 Post의 blocks, stickers 등 내용도 포함

        public static Response from(Community community, boolean isLiked) {
            return Response.builder()
                    .communityId(community.getId())
                    .postId(community.getPost().getId())
                    .title(community.getPost().getTitle())
                    .writerNickname(community.getUser().getUserProfile() != null
                            ? community.getUser().getUserProfile().getNickname()
                            : "Unknown") // 프로필 null 체크
                    .viewCount(community.getViewCount())
                    .likeCount(community.getLikeCount())
                    .isPublic(community.getPost().getIsPublic())
                    .isLiked(isLiked) // [추가]
                    .createdAt(community.getCreatedAt())
                    .build();
        }

        // 기존 코드 호환용 (isLiked 없이 호출 시 false 처리)
        public static Response from(Community community) {
            return from(community, false);
        }
    }
}