package com.project.byeoryback.domain.payment.service;

import com.project.byeoryback.domain.payment.dto.KakaopayApproveResponse;
import com.project.byeoryback.domain.payment.dto.KakaopayReadyResponse;
import com.project.byeoryback.domain.payment.entity.Payment;
import com.project.byeoryback.domain.payment.entity.PaymentStatus;
import com.project.byeoryback.domain.payment.repository.PaymentRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaopayService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakaopay.secret.key}")
    private String secretKey;

    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.api.url}")
    private String apiUrl;

    @Value("${kakaopay.approval.url}")
    private String approvalUrl;

    @Value("${kakaopay.cancel.url}")
    private String cancelUrl;

    @Value("${kakaopay.fail.url}")
    private String failUrl;

    public KakaopayReadyResponse ready(User user, Long amount) {
        String orderId = UUID.randomUUID().toString();

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", user.getId().toString());
        params.put("item_name", "Credit Charge");
        params.put("quantity", 1);
        params.put("total_amount", amount.intValue());
        params.put("tax_free_amount", 0);
        params.put("approval_url", approvalUrl);
        params.put("cancel_url", cancelUrl);
        params.put("fail_url", failUrl);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, getHeaders());

        try {
            KakaopayReadyResponse response = restTemplate.postForObject(apiUrl + "/ready", request,
                    KakaopayReadyResponse.class);

            if (response != null && response.getTid() != null) {
                Payment payment = Payment.builder()
                        .tid(response.getTid())
                        .orderId(orderId)
                        .user(user)
                        .amount(amount)
                        .status(PaymentStatus.READY)
                        .createdAt(LocalDateTime.now())
                        .build();
                paymentRepository.save(payment);
            }

            return response;
        } catch (HttpStatusCodeException e) {
            log.error("KakaoPay Ready API Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("KakaoPay Ready Error", e);
            throw e;
        }
    }

    public KakaopayApproveResponse approve(String pgToken, String tid) {
        Payment payment = paymentRepository.findByTid(tid)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", payment.getOrderId());
        params.put("partner_user_id", payment.getUser().getId().toString());
        params.put("pg_token", pgToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, getHeaders());

        try {
            KakaopayApproveResponse response = restTemplate.postForObject(apiUrl + "/approve", request,
                    KakaopayApproveResponse.class);

            if (response != null) {
                payment.approve();
                userService.addCredits(payment.getUser().getId(), payment.getAmount());
            }

            return response;
        } catch (HttpStatusCodeException e) {
            log.error("KakaoPay Approve API Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("KakaoPay Approve Error", e);
            throw e;
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
