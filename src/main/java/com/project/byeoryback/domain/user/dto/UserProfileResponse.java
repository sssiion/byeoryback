package com.project.byeoryback.domain.user.dto;

import com.project.byeoryback.domain.user.enums.Gender;
import java.time.LocalDate;

public record UserProfileResponse(
                Long id,
                String profilePhoto,
                String name,
                String nickname,
                String email,
                LocalDate birthDate,
                String phone,
                Gender gender,
                String bio,
                Long credits) {
}
