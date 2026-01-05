package com.project.byeoryback.domain.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketItemRequest {
    private String name;
    private Long price;
    private String category;
    private String contentJson;
}
