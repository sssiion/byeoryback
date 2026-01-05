package com.project.byeoryback.domain.market.repository;

import com.project.byeoryback.domain.market.entity.MarketItem;
import com.project.byeoryback.domain.market.entity.MarketItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface MarketItemRepository extends JpaRepository<MarketItem, Long> {
    List<MarketItem> findByStatusOrderByCreatedAtDesc(MarketItemStatus status);

    List<MarketItem> findBySellerIdAndStatus(Long sellerId, MarketItemStatus status);

    List<MarketItem> findBySellerId(Long sellerId);

    Page<MarketItem> findByNameContainingAndStatus(String name, MarketItemStatus status, Pageable pageable);

    Page<MarketItem> findByStatus(MarketItemStatus status, Pageable pageable);

    boolean existsBySellerIdAndReferenceIdAndStatus(Long sellerId, String referenceId, MarketItemStatus status);

    boolean existsByReferenceId(String referenceId);
}
