package com.project.byeoryback.domain.quest.enums;

public enum QuestType {
    DAILY_LOGIN(50L),
    DAILY_TOUCH(10L), // Widget interaction
    DAILY_PLAYTIME_30MIN(50L),
    DAILY_PLAYTIME_1HR(100L),
    DAILY_POST_WRITE(100L);

    private final Long rewardAmount;

    QuestType(Long rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public Long getRewardAmount() {
        return rewardAmount;
    }
}
