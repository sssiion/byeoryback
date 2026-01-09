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
    private final com.project.byeoryback.domain.post.repository.PostTemplateRepository postTemplateRepository;

    @Transactional(readOnly = true)
    public Page<MarketItemResponse> getAllOnSaleItems(String keyword, Long sellerId, List<String> tags, Boolean isFree,
            String category,
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

        if (Boolean.TRUE.equals(isFree)) {
            spec = spec.and(com.project.byeoryback.domain.market.repository.MarketItemSpecification.isFree());
        }

        if (category != null && !category.trim().isEmpty() && !category.equals("all")) {
            spec = spec
                    .and(com.project.byeoryback.domain.market.repository.MarketItemSpecification.hasCategory(category));
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

    @Transactional(readOnly = true)
    public MarketItemResponse getMarketItem(Long itemId) {
        MarketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        return MarketItemResponse.from(item, transactionRepository.countByItemId(item.getId()));
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

        if (item.getStatus() != MarketItemStatus.ON_SALE) {
            throw new IllegalStateException("Item is not on sale");
        }

        if (item.getSeller() != null && item.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("본인의 아이템은 구매할 수 없습니다.");
        }

        if (transactionRepository.existsByBuyerIdAndItemId(userId, itemId)) {
            throw new IllegalStateException("이미 구매한 아이템입니다.");
        }

        long price = item.getPrice();
        if (buyer.getCredits() < price) {
            throw new IllegalStateException("크레딧이 부족합니다.");
        }

        // Transaction
        buyer.spendCredits(price); // deduct
        if (item.getSeller() != null) {
            item.getSeller().addCredits(price); // add to seller
        }

        MarketTransaction transaction = MarketTransaction.builder()
                .item(item)
                .buyer(buyer)
                .seller(item.getSeller())
                .price(price)
                .transactionDate(java.time.LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // [추가됨] 템플릿 구매 시 자동으로 '내 템플릿'으로 복제 (Snapshot)
        if ("template_post".equalsIgnoreCase(item.getCategory()) || "TEMPLATE".equalsIgnoreCase(item.getCategory())) {
            createTemplateFromMarketItem(buyer, item);
        }
    }

    @Transactional
    public void buyItemByReferenceId(Long userId, String referenceId) {
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MarketItem item = itemRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (item.getStatus() != MarketItemStatus.ON_SALE) {
            throw new IllegalStateException("Item is not on sale");
        }

        if (item.getSeller() != null && item.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("본인의 아이템은 구매할 수 없습니다.");
        }

        if (transactionRepository.existsByBuyerIdAndItemId(userId, item.getId())) {
            return; // Already owned
        }

        long price = item.getPrice();
        if (buyer.getCredits() < price) {
            throw new IllegalStateException("크레딧이 부족합니다.");
        }

        // Transaction
        buyer.spendCredits(price); // deduct
        if (item.getSeller() != null) {
            item.getSeller().addCredits(price); // add to seller
        }

        MarketTransaction transaction = MarketTransaction.builder()
                .item(item)
                .buyer(buyer)
                .seller(item.getSeller())
                .price(price)
                .transactionDate(java.time.LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // [추가됨] 템플릿 구매 시 처리
        if ("template_post".equalsIgnoreCase(item.getCategory()) || "TEMPLATE".equalsIgnoreCase(item.getCategory())) {
            createTemplateFromMarketItem(buyer, item);
        }
    }

    private void createTemplateFromMarketItem(User buyer, MarketItem item) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.project.byeoryback.domain.post.dto.PostTemplateDto.CreateRequest templateData = mapper.readValue(
                    item.getContentJson(), com.project.byeoryback.domain.post.dto.PostTemplateDto.CreateRequest.class);

            com.project.byeoryback.domain.post.entity.PostTemplate template = com.project.byeoryback.domain.post.entity.PostTemplate
                    .builder()
                    .user(buyer)
                    .name(item.getName()) // Or templateData.getName()
                    .styles(templateData.getStyles())
                    .stickers(templateData.getStickers())
                    .floatingTexts(templateData.getFloatingTexts())
                    .floatingImages(templateData.getFloatingImages())
                    .defaultFontColor(templateData.getDefaultFontColor())
                    .sourceMarketItemId(item.getId())
                    .build();

            postTemplateRepository.save(template);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create template from market item", e);
        }
    }

    @Transactional
    public MarketItemResponse updateItem(Long userId, Long itemId, MarketItemRequest request) {
        MarketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("본인의 아이템만 수정할 수 있습니다.");
        }

        item.update(request.getName(), request.getPrice(), request.getContentJson(), request.getCategory());

        return MarketItemResponse.from(item, transactionRepository.countByItemId(item.getId()));
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
