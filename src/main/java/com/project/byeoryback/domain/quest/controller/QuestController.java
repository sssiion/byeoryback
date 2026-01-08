package com.project.byeoryback.domain.quest.controller;

import com.project.byeoryback.domain.quest.dto.QuestClaimRequest;
import com.project.byeoryback.domain.quest.service.QuestService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    @PostMapping("/claim")
    public ResponseEntity<?> claimQuest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody QuestClaimRequest request) {

        User user = userDetails.getUser();
        try {
            questService.claimQuest(user, request);
            return ResponseEntity.ok("Quest claimed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
