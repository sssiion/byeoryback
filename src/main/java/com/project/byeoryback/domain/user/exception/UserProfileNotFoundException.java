package com.project.byeoryback.domain.user.exception;

import lombok.Getter;

@Getter
public class UserProfileNotFoundException extends RuntimeException {
    private final Long userId;

    public UserProfileNotFoundException(Long userId) {
        super("Profile not found for user id: " + userId);
        this.userId = userId;
    }
}
