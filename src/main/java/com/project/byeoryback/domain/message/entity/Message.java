package com.project.byeoryback.domain.message.entity;

import com.project.byeoryback.domain.community.entity.Community;
import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages") // 테이블 이름은 복수형으로 보통 짓습니다
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용 (길어질 수 있으므로 Text 타입 추천)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 어떤 커뮤니티 게시글의 댓글인지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    // 댓글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 댓글 수정 메서드
    public void updateContent(String content) {
        this.content = content;
    }
}