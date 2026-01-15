package com.project.byeoryback.domain.payment.controller;

import com.project.byeoryback.domain.payment.dto.KakaopayApproveResponse;
import com.project.byeoryback.domain.payment.dto.KakaopayReadyRequest;
import com.project.byeoryback.domain.payment.dto.KakaopayReadyResponse;
import com.project.byeoryback.domain.payment.service.KakaopayService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class KakaopayController {

    private final KakaopayService kakaopayService;

    @PostMapping("/ready")
    public ResponseEntity<KakaopayReadyResponse> ready(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody KakaopayReadyRequest request) {
        log.info("Payment Ready Request - User: {}, Amount: {}", userDetails.getUser().getId(), request.getAmount());
        KakaopayReadyResponse response = kakaopayService.ready(userDetails.getUser(), request.getAmount());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<KakaopayApproveResponse> approve(
            @RequestBody Map<String, String> payload) {
        log.info("Payment Approve Request - TID: {}, pg_token: {}", payload.get("tid"), payload.get("pg_token"));
        String pgToken = payload.get("pg_token");
        String tid = payload.get("tid");
        KakaopayApproveResponse response = kakaopayService.approve(pgToken, tid);
        return ResponseEntity.ok(response);
    }
}
