package com.project.byeoryback.domain.quest.entity;

import com.project.byeoryback.domain.quest.enums.QuestType;
import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "quest_logs", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "quest_type", "quest_date" })
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "quest_type", nullable = false)
    private QuestType questType;

    @Column(name = "claimed_at", nullable = false)
    private LocalDateTime claimedAt;

    @Column(name = "quest_date", nullable = false)
    private LocalDate questDate; // The date this quest counts for (usually today)

}
