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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contentJson; // Stores JSON string of item data (e.g. image url, styles)

    @Column
    private String referenceId; // External ID to prevent duplicate listings (e.g. preset ID)

    @org.hibernate.annotations.Formula("(select count(r.id) from market_reviews r where r.market_item_id = {alias}.id)")
    private int reviewCount;

    @org.hibernate.annotations.Formula("(select coalesce(avg(r.rating), 0) from market_reviews r where r.market_item_id = {alias}.id)")
    private Double averageRating;

    @org.hibernate.annotations.Formula("(select count(t.id) from market_transactions t where t.item_id = {alias}.id)")
    private int salesCount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarketItemStatus status = MarketItemStatus.ON_SALE;

    @CreatedDate
    private LocalDateTime createdAt;

    public void cancelSale() {
        this.status = MarketItemStatus.CANCELLED;
    }

    public void update(String name, Long price, String contentJson, String category, String description) {
        if (name != null)
            this.name = name;
        if (price != null)
            this.price = price;
        if (contentJson != null)
            this.contentJson = contentJson;
        if (category != null)
            this.category = category;
        if (description != null)
            this.description = description;
    }
}
