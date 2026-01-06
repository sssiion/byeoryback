package com.project.byeoryback.domain.market.controller;

import com.project.byeoryback.domain.market.dto.ReviewRequest;
import com.project.byeoryback.domain.market.dto.ReviewResponse;
import com.project.byeoryback.domain.market.service.ReviewService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReviewRequest request) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(reviewService.createReview(user.getId(), request));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long itemId) {
        return ResponseEntity.ok(reviewService.getReviews(itemId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        reviewService.deleteReview(user.getId(), reviewId);
        return ResponseEntity.ok().build();
    }
}
