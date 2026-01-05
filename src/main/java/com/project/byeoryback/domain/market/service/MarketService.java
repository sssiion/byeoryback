package com.project.byeoryback.domain.market.service;

import com.project.byeoryback.domain.market.dto.MarketItemRequest;
import com.project.byeoryback.domain.market.dto.MarketItemResponse;
import com.project.byeoryback.domain.market.entity.MarketItem;
import com.project.byeoryback.domain.market.entity.MarketItemStatus;
import com.project.byeoryback.domain.market.entity.MarketTransaction;
import com.project.byeoryback.domain.market.repository.MarketItemRepository;
import com.project.byeoryback.domain.market.repository.MarketTransactionRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketItemRepository itemRepository;
    private final MarketTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MarketItemResponse> getAllOnSaleItems() {
        return itemRepository.findByStatusOrderByCreatedAtDesc(MarketItemStatus.ON_SALE)
                .stream()
                .map(MarketItemResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MarketItemResponse> getMySellingItems(Long userId) {
        return itemRepository.findBySellerId(userId)
                .stream()
                .map(MarketItemResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MarketItemResponse> getMyPurchasedItems(Long userId) {
        return transactionRepository.findByBuyerIdOrderByTransactionDateDesc(userId)
                .stream()
                .map(t -> MarketItemResponse.from(t.getItem()))
                .collect(Collectors.toList());
    }

    @Transactional
    public MarketItemResponse registerItem(Long userId, MarketItemRequest request) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MarketItem item = MarketItem.builder()
                .seller(seller)
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .contentJson(request.getContentJson())
                .status(MarketItemStatus.ON_SALE)
                .build();

        return MarketItemResponse.from(itemRepository.save(item));
    }

    @Transactional
    public void buyItem(Long userId, Long itemId) {
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MarketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (item.getStatus() == MarketItemStatus.SOLD) {
            throw new IllegalStateException("Item is already sold");
        }

        if (item.getSeller() != null && item.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("Cannot buy your own item");
        }

        // Check Credits
        if (!buyer.spendCredits(item.getPrice())) {
            throw new IllegalStateException("Not enough credits");
        }

        // Process Transaction
        MarketTransaction transaction = MarketTransaction.builder()
                .buyer(buyer)
                .seller(item.getSeller())
                .item(item)
                .price(item.getPrice())
                .build();

        transactionRepository.save(transaction);
        userRepository.save(buyer); // Update buyer credits

        // If it's a User Seller (not System), transfer credits and mark sold
        if (item.getSeller() != null) {
            User seller = item.getSeller();
            seller.addCredits(item.getPrice());
            userRepository.save(seller); // Update seller credits

            item.markAsSold();
        } else {
            // System Item - Infinite Stock, Credits disappear (burned)
        }
    }

    @Transactional
    public void cancelItem(Long userId, Long itemId) {
        MarketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("Not authorized to cancel this item");
        }

        if (item.getStatus() == MarketItemStatus.SOLD) {
            throw new IllegalStateException("Cannot cancel sold item");
        }

        item.cancelSale();
    }
}
