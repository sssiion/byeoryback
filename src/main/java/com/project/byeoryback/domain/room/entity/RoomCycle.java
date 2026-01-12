package com.project.byeoryback.domain.room.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room_cycle")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoomCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CycleType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CycleStatus status;

    @Column(nullable = false)
    private Integer currentTurnOrder;

    private LocalDateTime nextTurnTime;

    @Column(name = "post_id")
    private Long postId; // Link to the shared content (Rolling Paper)

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("turnOrder ASC")
    @BatchSize(size = 100)
    @Builder.Default
    private List<RoomCycleMember> members = new ArrayList<>();

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

    public void addMember(RoomCycleMember member) {
        this.members.add(member);
        member.assignCycle(this);
    }

    public void nextTurn(LocalDateTime nextDeadline) {
        this.currentTurnOrder++;
        this.nextTurnTime = nextDeadline;
    }

    public void complete() {
        this.status = CycleStatus.COMPLETED;
        this.nextTurnTime = null;
    }

    public void updatePostId(Long postId) {
        this.postId = postId;
    }

    public enum CycleType {
        ROLLING_PAPER, EXCHANGE_DIARY
    }

    public enum CycleStatus {
        IN_PROGRESS, COMPLETED
    }
}
