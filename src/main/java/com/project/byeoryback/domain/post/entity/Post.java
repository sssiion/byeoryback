package com.project.byeoryback.domain.post.entity;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.album.entity.AlbumContent;
import com.project.byeoryback.domain.hashtag.entity.PostHashtag;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostHashtag> postHashtags = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "childPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AlbumContent> albumContents = new java.util.ArrayList<>();

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
            List<FloatingItem> floatingTexts, List<FloatingItem> floatingImages, Boolean isFavorite, String mode) {
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
    }

    public void clearPostHashtags() {
        this.postHashtags.clear();
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtags.add(postHashtag);
    }
}
