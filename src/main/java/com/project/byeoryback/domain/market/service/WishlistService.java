package com.project.byeoryback.domain.market.service;

import com.project.byeoryback.domain.market.dto.MarketItemResponse;
import com.project.byeoryback.domain.market.entity.MarketItem;
import com.project.byeoryback.domain.market.entity.Wishlist;
import com.project.byeoryback.domain.market.repository.MarketItemRepository;
import com.project.byeoryback.domain.market.repository.MarketTransactionRepository;
import com.project.byeoryback.domain.market.repository.WishlistRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MarketItemRepository itemRepository;
    private final UserRepository userRepository;
    private final MarketTransactionRepository transactionRepository;

    @Transactional
    public boolean toggleWishlist(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MarketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Optional<Wishlist> existing = wishlistRepository.findByUserIdAndMarketItemId(userId, itemId);
        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return false; // Removed
        } else {
            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .marketItem(item)
                    .build();
            try {
                wishlistRepository.save(wishlist);
                return true; // Added
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // Concurrency: Item already added by another thread.
                // We consider it "Added" effectively.
                return true;
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MarketItemResponse> getMyWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(w -> MarketItemResponse.from(w.getMarketItem(),
                        transactionRepository.countByItemId(w.getMarketItem().getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Long> getMyWishlistIds(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(w -> w.getMarketItem().getId())
                .collect(Collectors.toList());
    }
}
