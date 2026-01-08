package com.project.byeoryback.domain.quest.service;

import com.project.byeoryback.domain.post.repository.PostRepository;
import com.project.byeoryback.domain.quest.dto.QuestClaimRequest;
import com.project.byeoryback.domain.quest.entity.QuestLog;
import com.project.byeoryback.domain.quest.enums.QuestType;
import com.project.byeoryback.domain.quest.repository.QuestLogRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestService {

    private final QuestLogRepository questLogRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void claimQuest(User principalUser, QuestClaimRequest request) {
        User user = userRepository.findById(principalUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        QuestType type = request.getQuestType();
        LocalDate today = LocalDate.now();

        // 1. Check if already claimed today
        if (questLogRepository.existsByUserAndQuestTypeAndQuestDate(user, type, today)) {
            throw new IllegalArgumentException("Already claimed this quest today.");
        }

        // 2. Check conditions
        validateQuestConditions(user, type, today);

        // 3. Give Reward
        user.addCredits(type.getRewardAmount());
        userRepository.save(user);

        // 4. Log the claim
        QuestLog log = QuestLog.builder()
                .user(user)
                .questType(type)
                .questDate(today)
                .claimedAt(LocalDateTime.now())
                .build();
        questLogRepository.save(log);
    }

    private void validateQuestConditions(User user, QuestType type, LocalDate today) {
        switch (type) {
            case DAILY_LOGIN:
            case DAILY_TOUCH:
                // Implicitly met if they are calling this API (Client-side validation trusted
                // for touch)
                break;
            case DAILY_PLAYTIME_30MIN:
                // 30 minutes = 1800 seconds
                if (user.getTodayPlayTime() < 1800) {
                    throw new IllegalArgumentException("Daily playtime condition not met (need 30 mins).");
                }
                break;
            case DAILY_PLAYTIME_1HR:
                // 1 hour = 3600 seconds
                if (user.getTodayPlayTime() < 3600) {
                    throw new IllegalArgumentException("Daily playtime condition not met (need 1 hour).");
                }
                break;
            case DAILY_POST_WRITE:
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
                if (!postRepository.existsByUserIdAndCreatedAtBetween(user.getId(), startOfDay, endOfDay)) {
                    throw new IllegalArgumentException("No post written today.");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown quest type.");
        }
    }
}
