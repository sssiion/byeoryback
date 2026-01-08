package com.project.byeoryback.domain.post.entity;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.album.entity.AlbumContent;
import com.project.byeoryback.domain.hashtag.entity.PostHashtag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.project.byeoryback.domain.post.entity.PostStat;
import com.project.byeoryback.domain.post.entity.PostLike;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "posts", indexes = {
        // 1. 아까 추가한 내 글 조회용
        @Index(name = "idx_posts_user_created", columnList = "user_id, created_at DESC"),

        // 2. [NEW] 지금 추가할 커뮤니티 조회용
        @Index(name = "idx_posts_public_created", columnList = "is_public, created_at DESC")
})
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

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private java.util.Map<String, Object> titleStyles;

    @Column(length = 10)
    private String mode; // "AUTO", "MANUAL"

    // 작성자 연결 (User 엔티티와 관계 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private com.project.byeoryback.domain.room.entity.Room room;

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

    // [추가됨] 공개 여부 (기본값 true 또는 false 설정)
    @Column(nullable = false)
    @Builder.Default
    private Boolean isPublic = true;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostHashtag> postHashtags = new java.util.ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostStat postStat;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "childPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AlbumContent> albumContents = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<com.project.byeoryback.domain.message.entity.Message> messages = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFavorite = false;

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

    public void update(String title, java.util.Map<String, Object> titleStyles, List<Block> blocks,
            List<FloatingItem> stickers,
            List<FloatingItem> floatingTexts, List<FloatingItem> floatingImages, Boolean isFavorite, String mode,
            Boolean isPublic) {
        this.title = title;
        this.titleStyles = titleStyles;
        this.blocks = blocks;
        this.stickers = stickers;
        this.floatingTexts = floatingTexts;
        this.floatingImages = floatingImages;
        if (isFavorite != null) {
            this.isFavorite = isFavorite;
        }
        this.mode = mode;
        this.mode = mode;
        if (isPublic != null)
            this.isPublic = isPublic;
    }

    public void clearPostHashtags() {
        this.postHashtags.clear();
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtags.add(postHashtag);
    }
}
