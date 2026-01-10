package com.project.byeoryback.domain.quest.repository;

import com.project.byeoryback.domain.quest.entity.QuestLog;
import com.project.byeoryback.domain.quest.enums.QuestType;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface QuestLogRepository extends JpaRepository<QuestLog, Long> {
    boolean existsByUserAndQuestTypeAndQuestDate(User user, QuestType questType, LocalDate questDate);

    void deleteByUser(User user);
}
