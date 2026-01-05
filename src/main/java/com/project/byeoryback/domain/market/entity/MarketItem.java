package com.project.byeoryback.domain.market.entity;

import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "market_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MarketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String category; // e.g. "sticker", "theme"

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contentJson; // Stores JSON string of item data (e.g. image url, styles)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MarketItemStatus status = MarketItemStatus.ON_SALE;

    @CreatedDate
    private LocalDateTime createdAt;

    public void markAsSold() {
        this.status = MarketItemStatus.SOLD;
    }

    public void cancelSale() {
        this.status = MarketItemStatus.CANCELLED;
    }
}
