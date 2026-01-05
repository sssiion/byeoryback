package com.project.byeoryback.domain.market.controller;

import com.project.byeoryback.domain.market.dto.MarketItemResponse;
import com.project.byeoryback.domain.market.service.WishlistService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;

    @PostMapping("/{itemId}")
    public ResponseEntity<?> toggleWishlist(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        boolean isAdded = wishlistService.toggleWishlist(user.getId(), itemId);
        return ResponseEntity.ok(Map.of("added", isAdded));
    }

    @GetMapping
    public ResponseEntity<List<MarketItemResponse>> getMyWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(wishlistService.getMyWishlist(user.getId()));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getMyWishlistIds(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(wishlistService.getMyWishlistIds(user.getId()));
    }
}
