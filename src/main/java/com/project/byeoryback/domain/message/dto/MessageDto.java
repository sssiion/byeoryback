package com.project.byeoryback.domain.message.dto;

import com.project.byeoryback.domain.message.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MessageDto {

    // [Request] 댓글 작성/수정 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String content;
    }

    // [Response] 댓글 응답
    @Getter
    @Builder
    public static class Response {
        private Long messageId;
        private Long communityId;
        private Long userId;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;

        public static Response from(Message message) {
            return Response.builder()
                    .messageId(message.getId())
                    .communityId(message.getCommunity().getId())
                    .userId(message.getUser().getId())
                    .nickname(message.getUser().getUserProfile() != null
                            ? message.getUser().getUserProfile().getNickname() : "Unknown")
                    .content(message.getContent())
                    .createdAt(message.getCreatedAt())
                    .build();
        }
    }
}