package com.project.byeoryback.domain.post.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // 작성자 연결 (User 엔티티와 관계 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 1. 블록 리스트 (JSON 저장)
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON) // Hibernate 6버전 이상에서 JSON 처리 시 권장
    private List<Block> blocks;

    // 2. 스티커 리스트
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FloatingItem> stickers;

    // 3. 텍스트 메모 리스트
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FloatingItem> floatingTexts;

    // 4. 자유 이미지 리스트 (URL 포함)
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FloatingItem> floatingImages;

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

    public void update(String title, List<Block> blocks, List<FloatingItem> stickers,
            List<FloatingItem> floatingTexts, List<FloatingItem> floatingImages) {
        this.title = title;
        this.blocks = blocks;
        this.stickers = stickers;
        this.floatingTexts = floatingTexts;
        this.floatingImages = floatingImages;
    }
}
