package com.project.byeoryback.domain.album.entity;

import com.project.byeoryback.domain.hashtag.entity.Hashtag;
import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;

@Entity
@Table(name = "albums")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representative_hashtag_id")
    private Hashtag representativeHashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Album parent;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFavorite = false;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> coverConfig;

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

    public void update(String name, Album parent, Hashtag representativeHashtag, Boolean isFavorite,
            Map<String, Object> coverConfig) {
        this.name = name;
        this.parent = parent;
        this.representativeHashtag = representativeHashtag;
        if (isFavorite != null) {
            this.isFavorite = isFavorite;
        }
        if (coverConfig != null) {
            this.coverConfig = coverConfig;
        }
    }
}
