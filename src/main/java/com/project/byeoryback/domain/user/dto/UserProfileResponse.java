package com.project.byeoryback.domain.user.dto;

import com.project.byeoryback.domain.user.enums.Gender;
import java.time.LocalDate;

public record UserProfileResponse(
        String profilePhoto,
        String name,
        String nickname,
        LocalDate birthDate,
        String phone,
        Gender gender,
        String bio) {
}
