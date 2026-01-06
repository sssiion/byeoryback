package com.project.byeoryback.domain.message.controller;

import com.project.byeoryback.domain.message.dto.MessageDto;
import com.project.byeoryback.domain.message.service.MessageService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository; // 유저 조회를 위해 임시 사용
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {

        private final MessageService messageService;
        private final UserRepository userRepository;

        /**
         * 댓글 목록 조회 (특정 커뮤니티 글의 댓글들)
         * GET /api/communities/{communityId}/messages
         */
        @GetMapping("/communities/{communityId}/messages")
        public ResponseEntity<List<MessageDto.Response>> getMessages(@PathVariable Long communityId) {
                List<MessageDto.Response> responses = messageService.getMessagesByCommunityId(communityId)
                                .stream()
                                .map(MessageDto.Response::from)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        /**
         * 댓글 작성
         * POST /api/communities/{communityId}/messages
         */
        @PostMapping("/communities/{communityId}/messages")
        public ResponseEntity<MessageDto.Response> createMessage(
                        @PathVariable Long communityId,
                        @RequestBody MessageDto.Request request,
                        @RequestParam Long userId // [TODO] 실제론 @AuthenticationPrincipal 등으로 교체 권장
        ) {
                // 유저 조회 (SecurityContextHolder 사용 시 생략 가능)
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

                var message = messageService.createMessage(user, communityId, request.getContent());
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
                        @RequestParam Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

                var updatedMessage = messageService.updateMessage(user, messageId, request.getContent());
                return ResponseEntity.ok(MessageDto.Response.from(updatedMessage));
        }

        /**
         * 댓글 삭제
         * DELETE /api/messages/{messageId}
         */
        @DeleteMapping("/messages/{messageId}")
        public ResponseEntity<Void> deleteMessage(
                        @PathVariable Long messageId,
                        @RequestParam Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

                messageService.deleteMessage(user, messageId);
                return ResponseEntity.noContent().build();
        }
}
