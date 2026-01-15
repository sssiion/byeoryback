package com.project.byeoryback.domain.payment.repository;

import com.project.byeoryback.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTid(String tid);

    Optional<Payment> findByOrderId(String orderId);
}
