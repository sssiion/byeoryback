package com.project.byeoryback.domain.market.controller;

import com.project.byeoryback.domain.market.dto.MarketItemRequest;
import com.project.byeoryback.domain.market.dto.MarketItemResponse;
import com.project.byeoryback.domain.market.service.MarketService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<MarketItemResponse>> getAllOnSaleItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Boolean isFree,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return ResponseEntity.ok(marketService.getAllOnSaleItems(keyword, sellerId, tags, isFree, category, pageable));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<MarketItemResponse> getMarketItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(marketService.getMarketItem(itemId));
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

    @PutMapping("/items/{itemId}")
    public ResponseEntity<MarketItemResponse> updateItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long itemId,
            @RequestBody MarketItemRequest request) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        return ResponseEntity.ok(marketService.updateItem(userDetails.getUser().getId(), itemId, request));
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

    @PostMapping("/buy/ref/{referenceId}")
    public ResponseEntity<Void> buyItemByRef(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String referenceId) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();
        try {
            marketService.buyItemByReferenceId(userDetails.getUser().getId(), referenceId);
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
