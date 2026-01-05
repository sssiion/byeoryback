package com.project.byeoryback.domain.market.controller;

import com.project.byeoryback.domain.market.dto.MarketItemRequest;
import com.project.byeoryback.domain.market.dto.MarketItemResponse;
import com.project.byeoryback.domain.market.service.MarketService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/items")
    public ResponseEntity<List<MarketItemResponse>> getAllOnSaleItems() {
        return ResponseEntity.ok(marketService.getAllOnSaleItems());
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<MarketItemResponse>> getMySellingItems(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        return ResponseEntity.ok(marketService.getMySellingItems(userDetails.getUser().getId()));
    }

    @GetMapping("/purchased")
    public ResponseEntity<List<MarketItemResponse>> getMyPurchasedItems(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        return ResponseEntity.ok(marketService.getMyPurchasedItems(userDetails.getUser().getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<MarketItemResponse> registerItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MarketItemRequest request) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        return ResponseEntity.ok(marketService.registerItem(userDetails.getUser().getId(), request));
    }

    @PostMapping("/buy/{itemId}")
    public ResponseEntity<Void> buyItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long itemId) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        try {
            marketService.buyItem(userDetails.getUser().getId(), itemId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cancel/{itemId}")
    public ResponseEntity<Void> cancelItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long itemId) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        try {
            marketService.cancelItem(userDetails.getUser().getId(), itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
