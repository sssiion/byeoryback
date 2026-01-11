package com.project.byeoryback.domain.setting.widget.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "widget_settings")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 위젯 배치 정보 (JSON)
    // 예: [
    // { "i": "welcome-1", "x": 0, "y": 0, "w": 2, "h": 1, "type": "welcome",
    // "props": {} },
    // { "i": "daily-diary-1", "x": 0, "y": 1, "w": 2, "h": 2, "type":
    // "daily-diary", "props": {} }
    // ]
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> layoutData;

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLayout(List<Map<String, Object>> layoutData) {
        this.layoutData = layoutData;
    }
}
