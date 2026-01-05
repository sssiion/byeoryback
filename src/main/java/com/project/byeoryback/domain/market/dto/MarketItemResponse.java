package com.project.byeoryback.domain.market.dto;

import com.project.byeoryback.domain.market.entity.MarketItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketItemResponse {
    private Long id;
    private Long sellerId;
    private String sellerName; // Optional, if we want to show who sold it
    private String name;
    private Long price;
    private String category;
    private String contentJson;
    private String status;
    private LocalDateTime createdAt;

    public static MarketItemResponse from(MarketItem item) {
        return MarketItemResponse.builder()
                .id(item.getId())
                .sellerId(item.getSeller() != null ? item.getSeller().getId() : null)
                .sellerName(item.getSeller() != null ? item.getSeller().getEmail() : "System") // Using email for name
                                                                                               // for now
                .name(item.getName())
                .price(item.getPrice())
                .category(item.getCategory())
                .contentJson(item.getContentJson())
                .status(item.getStatus().name())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
