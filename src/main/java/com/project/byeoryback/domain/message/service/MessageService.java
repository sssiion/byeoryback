package com.project.byeoryback.domain.message.service;


import com.project.byeoryback.domain.community.entity.Community;
import com.project.byeoryback.domain.community.repository.CommunityRepository;
import com.project.byeoryback.domain.message.entity.Message;
import com.project.byeoryback.domain.message.repository.MessageRepository;
import com.project.byeoryback.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final CommunityRepository communityRepository;

    // 1. 댓글 생성
    @Transactional
    public Message createMessage(User user, Long communityId, String content) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 글이 없습니다."));

        Message message = Message.builder()
                .community(community)
                .user(user)
                .content(content)
                .build();

        return messageRepository.save(message);
    }

    // 2. 댓글 수정
    @Transactional
    public Message updateMessage(User user, Long messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (!message.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        message.updateContent(newContent);
        return message;
    }

    // 3. 댓글 삭제
    @Transactional
    public void deleteMessage(User user, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (!message.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        messageRepository.delete(message);
    }

    // 4. 특정 커뮤니티 글의 댓글 목록 조회
    public List<Message> getMessagesByCommunityId(Long communityId) {
        return messageRepository.findByCommunityIdOrderByCreatedAtAsc(communityId);
    }
}