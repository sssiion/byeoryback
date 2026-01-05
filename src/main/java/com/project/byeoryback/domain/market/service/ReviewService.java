package com.project.byeoryback.domain.market.service;

import com.project.byeoryback.domain.market.dto.ReviewRequest;
import com.project.byeoryback.domain.market.dto.ReviewResponse;
import com.project.byeoryback.domain.market.entity.MarketItem;
import com.project.byeoryback.domain.market.entity.Review;
import com.project.byeoryback.domain.market.repository.MarketItemRepository;
import com.project.byeoryback.domain.market.repository.ReviewRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MarketItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MarketItem item = itemRepository.findById(request.getMarketItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (reviewRepository.existsByUserIdAndMarketItemId(userId, request.getMarketItemId())) {
            throw new IllegalStateException("이미 리뷰를 작성했습니다.");
        }

        Review review = Review.builder()
                .user(user)
                .marketItem(item)
                .content(request.getContent())
                .rating(request.getRating())
                .build();

        reviewRepository.save(review);

        return ReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long itemId) {
        return reviewRepository.findByMarketItemIdOrderByCreatedAtDesc(itemId).stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }
}
