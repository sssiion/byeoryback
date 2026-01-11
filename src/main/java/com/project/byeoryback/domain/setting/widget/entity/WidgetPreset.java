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
@Table(name = "widget_presets")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetPreset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private LocalDateTime createdAt;

    // 위젯 배치 정보 (JSON)
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> layoutData;

    // Grid Size (JSON) - Optional but good to have if we support resizing grid
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Integer> gridSize;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void update(String name, List<Map<String, Object>> layoutData, Map<String, Integer> gridSize) {
        if (name != null)
            this.name = name;
        this.layoutData = layoutData;
        this.gridSize = gridSize;
        this.createdAt = LocalDateTime.now();
    }
}
