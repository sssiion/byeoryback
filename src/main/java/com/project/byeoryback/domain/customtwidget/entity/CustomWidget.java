package com.project.byeoryback.domain.customtwidget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "widgets")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomWidget {

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
    @JoinColumn(name = "users")
    @JsonIgnore
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

    // 🌟 [NEW] 위젯 사이즈 추가 (예: "2x2", "4x2")
    @Column(length = 10)
    private String defaultSize;

    // 👇 [추가] 도형/꾸미기 요소 저장용 필드
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> decorations; // Decorations List

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, String defaultSize, Map<String, Object> content, Map<String, Object> styles,
            List<Map<String, Object>> decorations, boolean isShared) {
        this.name = name;
        this.defaultSize = defaultSize; // 사이즈 업데이트
        this.content = content;
        this.styles = styles;
        this.decorations = decorations;
        this.isShared = isShared;
        this.updatedAt = LocalDateTime.now(); // 수정 시간 갱신
    }

    public void incrementDownloadCount() {
        this.downloadCount++;
    }
}