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
        private LocalDateTime createdAt;
        // 필요하다면 Post의 blocks, stickers 등 내용도 포함

        public static Response from(Community community) {
            return Response.builder()
                    .communityId(community.getId())
                    .postId(community.getPost().getId())
                    .title(community.getPost().getTitle())
                    .writerNickname(community.getUser().getUserProfile() != null
                            ? community.getUser().getUserProfile().getNickname() : "Unknown") // 프로필 null 체크
                    .viewCount(community.getViewCount())
                    .likeCount(community.getLikeCount())
                    .isPublic(community.getPost().getIsPublic())
                    .createdAt(community.getCreatedAt())
                    .build();
        }
    }
}