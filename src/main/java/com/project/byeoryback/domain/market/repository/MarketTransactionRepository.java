package com.project.byeoryback.domain.market.repository;

import com.project.byeoryback.domain.market.entity.MarketTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketTransactionRepository extends JpaRepository<MarketTransaction, Long> {
    List<MarketTransaction> findByBuyerIdOrderByTransactionDateDesc(Long buyerId);

    List<MarketTransaction> findBySellerIdOrderByTransactionDateDesc(Long sellerId);
}
