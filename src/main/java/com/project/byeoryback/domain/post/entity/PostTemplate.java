package com.project.byeoryback.domain.post.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "post_templates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    // Paper Design: Background, Border, etc.
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> styles;

    // Decorative Elements
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FloatingItem> stickers;

    // Layout Placeholders
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FloatingItem> floatingTexts;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FloatingItem> floatingImages;

    // Auto-Contrast: Default font color for this template
    @Column(length = 7)
    private String defaultFontColor;

    // Tags for filtering (e.g. "acquired")
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;

    @Column
    private String thumbnailUrl;

    // Link to Market Item if this is a purchased template (optional)
    @Column
    private Long sourceMarketItemId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void update(String name, Map<String, Object> styles, List<FloatingItem> stickers,
            List<FloatingItem> floatingTexts, List<FloatingItem> floatingImages, String defaultFontColor,
            String thumbnailUrl) {
        if (name != null)
            this.name = name;
        if (styles != null)
            this.styles = styles;
        if (stickers != null)
            this.stickers = stickers;
        if (floatingTexts != null)
            this.floatingTexts = floatingTexts;
        if (floatingImages != null)
            this.floatingImages = floatingImages;
        if (defaultFontColor != null)
            this.defaultFontColor = defaultFontColor;
        if (thumbnailUrl != null)
            this.thumbnailUrl = thumbnailUrl;
    }
}
