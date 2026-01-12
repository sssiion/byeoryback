package com.project.byeoryback.domain.message.controller;

import com.project.byeoryback.domain.message.dto.MessageDto;
import com.project.byeoryback.domain.message.service.MessageService;

import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {

        private final MessageService messageService;

        /**
         * 댓글 목록 조회 (특정 게시글의 댓글들)
         * GET /api/posts/{postId}/messages
         */
        @GetMapping("/posts/{postId}/messages")
        public ResponseEntity<List<MessageDto.Response>> getMessages(@PathVariable Long postId) {
                List<MessageDto.Response> responses = messageService.getMessagesByPostId(postId)
                                .stream()
                                .map(MessageDto.Response::from)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        /**
         * 댓글 작성
         * POST /api/posts/{postId}/messages
         */
        @PostMapping("/posts/{postId}/messages")
        public ResponseEntity<MessageDto.Response> createMessage(
                        @PathVariable Long postId,
                        @RequestBody MessageDto.Request request,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

                var message = messageService.createMessage(userDetails.getUser(), postId, request.getContent());
                return ResponseEntity.ok(MessageDto.Response.from(message));
        }

        /**
         * 댓글 수정
         * PATCH /api/messages/{messageId}
         */
        @PatchMapping("/messages/{messageId}")
        public ResponseEntity<MessageDto.Response> updateMessage(
                        @PathVariable Long messageId,
                        @RequestBody MessageDto.Request request,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

                var updatedMessage = messageService.updateMessage(userDetails.getUser(), messageId,
                                request.getContent());
                return ResponseEntity.ok(MessageDto.Response.from(updatedMessage));
        }

        /**
         * 댓글 삭제
         * DELETE /api/messages/{messageId}
         */
        @DeleteMapping("/messages/{messageId}")
        public ResponseEntity<Void> deleteMessage(
                        @PathVariable Long messageId,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

                messageService.deleteMessage(userDetails.getUser(), messageId);
                return ResponseEntity.noContent().build();
        }
}
