package com.project.byeoryback.domain.market.repository;

import com.project.byeoryback.domain.market.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);

    Optional<Wishlist> findByUserIdAndMarketItemId(Long userId, Long marketItemId);

    boolean existsByUserIdAndMarketItemId(Long userId, Long marketItemId);
}
