package com.project.byeoryback.domain.widget.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "widget_definitions", indexes = {
        @Index(name = "idx_widget_type", columnList = "widget_type"),
        @Index(name = "idx_widget_category", columnList = "category")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 위젯 식별자 (프론트엔드 컴포넌트 매핑용)
    // 예: "timeline", "scratch-card", "weather"
    @Column(name = "widget_type", nullable = false, length = 50, unique = true)
    private String widgetType;

    // 표시 이름 (예: "타임라인", "복권 긁기")
    @Column(nullable = false)
    private String label;

    // 설명
    @Column(length = 500)
    private String description;

    // 카테고리 (예: "Utility", "Interactive", "Decoration")
    @Column(nullable = false, length = 50)
    private String category;

    // 썸네일 경로 (예: "/widgets/thumbnails/timeline.png")
    @Column(length = 255)
    private String thumbnail;

    // 검색용 키워드 태그
    // 예: ["일정", "기록", "history"]
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> keywords;

    // 기본 사이즈 (예: "2x2")
    @Column(nullable = false, length = 10)
    private String defaultSize;

    // 지원하는 사이즈 목록
    // 예: [[1, 2], [2, 2], [2, 4]]
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<List<Integer>> validSizes;

    // 위젯 초기 데이터 (props로 전달될 기본값)
    // 예: { "title": "My Timeline", "events": [] }
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> defaultProps;

    // 시스템 기본 위젯 여부 (true면 모든 유저에게 보임, false면 특정 유저의 저장된 위젯)
    @Builder.Default
    @Column(nullable = false)
    private Boolean isSystem = true;

    // 커스텀 위젯일 경우 소유자 (시스템 위젯은 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
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
}