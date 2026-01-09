package com.project.byeoryback.domain.persona.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String analysisResult; // AI Analysis Result

    @Column(columnDefinition = "TEXT")
    private String emotionKeywords; // Extracted Keywords

    @Column(columnDefinition = "TEXT")
    private String excludedHashtags; // Hashtags to exclude from analysis (comma separated)

    @CreatedDate
    private LocalDateTime createdAt;
}
