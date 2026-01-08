package com.project.byeoryback.domain.quest.dto;

import com.project.byeoryback.domain.quest.enums.QuestType;
import lombok.Data;

@Data
public class QuestClaimRequest {
    private QuestType questType;
}
