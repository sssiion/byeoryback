package com.project.byeoryback.domain.market.repository;

import com.project.byeoryback.domain.market.entity.MarketTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketTransactionRepository extends JpaRepository<MarketTransaction, Long> {
    List<MarketTransaction> findByBuyerIdOrderByTransactionDateDesc(Long buyerId);

    List<MarketTransaction> findBySellerIdOrderByTransactionDateDesc(Long sellerId);

    long countByItemId(Long itemId);

    boolean existsByBuyerIdAndItemId(Long buyerId, Long itemId);

    @Query("SELECT COUNT(t) > 0 FROM MarketTransaction t JOIN t.item i WHERE t.buyer.id = :buyerId AND i.referenceId = :referenceId")
    boolean existsByBuyerIdAndItemReferenceId(@Param("buyerId") Long buyerId, @Param("referenceId") String referenceId);
}
