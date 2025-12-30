package com.project.byeoryback.domain.widget.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "widgets")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 위젯 이름 (예: 내 인생 영화 리스트)

    @Column(nullable = false)
    private String type; // book-info, movie-ticket 등

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> content; // 데이터

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> styles; // 스타일

    // 🌟 1. 작성자 연결 (제공해주신 User 엔티티 사용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🌟 2. 공유 여부 (true면 마켓에 공개)
    @Column(nullable = false)
    @Builder.Default
    private boolean isShared = false;

    // 🌟 3. (선택사항) 다른 사람이 가져간 횟수
    @Column(nullable = false)
    @Builder.Default
    private int downloadCount = 0;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, Map<String, Object> content, Map<String, Object> styles, boolean isShared) {
        this.name = name;
        this.content = content;
        this.styles = styles;
        this.isShared = isShared;
    }

    public void incrementDownloadCount() {
        this.downloadCount++;
    }
}