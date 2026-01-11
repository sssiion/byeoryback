package com.project.byeoryback.domain.setting.header;

import com.project.byeoryback.domain.setting.header.entity.HeaderSetting;

public record HeaderDto(boolean showTimer, boolean showCredit) {
    public static HeaderDto from(HeaderSetting setting) {
        return new HeaderDto(setting.isShowTimer(), setting.isShowCredit());
    }
}
