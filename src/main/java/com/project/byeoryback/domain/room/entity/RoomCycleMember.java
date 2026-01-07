package com.project.byeoryback.domain.room.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_cycle_member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoomCycleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private RoomCycle cycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer turnOrder;

    @Column(nullable = false)
    private boolean isCompleted;

    public void assignCycle(RoomCycle cycle) {
        this.cycle = cycle;
    }

    public void completeTurn() {
        this.isCompleted = true;
    }
}
