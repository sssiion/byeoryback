package com.project.byeoryback.domain.message.repository; // 패키지명 확인 필요 (보통 community 하위에 둘지 분리할지 결정)

import com.project.byeoryback.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByPostIdOrderByCreatedAtAsc(Long postId);

    // 유저 탈퇴 시 댓글 삭제용
    void deleteByUserId(Long userId);
}