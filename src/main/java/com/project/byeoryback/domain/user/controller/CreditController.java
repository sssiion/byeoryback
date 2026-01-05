package com.project.byeoryback.domain.user.controller;

import com.project.byeoryback.domain.user.service.UserService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CreditController {

    private final UserService userService;

    // Temporary endpoint to add credits from frontend (e.g. for Quests)
    // In production, Quests should be validated on backend.
    @PostMapping("/add")
    public ResponseEntity<Void> addCredits(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Long> payload) {
        Long userId = userDetails.getUser().getId();
        Long amount = payload.get("amount");
        userService.addCredits(userId, amount);
        return ResponseEntity.ok().build();
    }
}
