package com.project.byeoryback.domain.market.repository;

import com.project.byeoryback.domain.market.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMarketItemIdOrderByCreatedAtDesc(Long marketItemId);

    boolean existsByUserIdAndMarketItemId(Long userId, Long marketItemId);
}
