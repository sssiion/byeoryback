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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<MarketItemResponse> getAllOnSaleItems(String keyword, Long sellerId, List<String> tags,
            Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<MarketItem> spec = org.springframework.data.jpa.domain.Specification
                .where(com.project.byeoryback.domain.market.repository.MarketItemSpecification
                        .hasStatus(MarketItemStatus.ON_SALE));

        if (sellerId != null) {
            spec = spec
                    .and(com.project.byeoryback.domain.market.repository.MarketItemSpecification.hasSellerId(sellerId));
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and(
                    com.project.byeoryback.domain.market.repository.MarketItemSpecification.containsKeyword(keyword));
        }

        if (tags != null && !tags.isEmpty()) {
            spec = spec.and(com.project.byeoryback.domain.market.repository.MarketItemSpecification.containsTags(tags));
        }

        Page<MarketItem> items = itemRepository.findAll(spec, pageable);
        return items.map(item -> MarketItemResponse.from(item, transactionRepository.countByItemId(item.getId())));
    }

    @Transactional(readOnly = true)
    public List<MarketItemResponse> getMySellingItems(Long userId) {
        return itemRepository.findBySellerId(userId)
                .stream()
                .map(item -> MarketItemResponse.from(item, transactionRepository.countByItemId(item.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MarketItemResponse> getMyPurchasedItems(Long userId) {
        return transactionRepository.findByBuyerIdOrderByTransactionDateDesc(userId)
                .stream()
                .map(t -> MarketItemResponse.from(t.getItem(),
                        transactionRepository.countByItemId(t.getItem().getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public MarketItemResponse registerItem(Long userId, MarketItemRequest request) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getReferenceId() != null && !request.getReferenceId().isEmpty()) {
            if (itemRepository.existsBySellerIdAndReferenceIdAndStatus(userId, request.getReferenceId(),
                    MarketItemStatus.ON_SALE)) {
                throw new IllegalStateException("이미 마켓에 등록된 아이템입니다.");
            }
        }

        MarketItem item = MarketItem.builder()
                .seller(seller)
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .contentJson(request.getContentJson())
                .referenceId(request.getReferenceId())
                .status(MarketItemStatus.ON_SALE)
                .build();

        return MarketItemResponse.from(itemRepository.save(item), 0L);
    }

    @Transactional
    public void buyItem(Long userId, Long itemId) {
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MarketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (item.getStatus() == MarketItemStatus.CANCELLED) {
            throw new IllegalStateException("Item sale is cancelled");
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

            // item.markAsSold(); // REMOVED: Items are digital goods (unlimited stock)
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

        item.cancelSale();
    }
}
