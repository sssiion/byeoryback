package com.project.byeoryback.domain.pin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PinStatusResponse {
    private boolean isRegistered;
    private int failureCount;
    private boolean isLocked;
}
