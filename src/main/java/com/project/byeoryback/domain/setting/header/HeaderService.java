package com.project.byeoryback.domain.setting.header;

import com.project.byeoryback.domain.setting.header.entity.HeaderSetting;
import com.project.byeoryback.domain.setting.header.repository.HeaderSettingRepository;
import com.project.byeoryback.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeaderService {

    private final HeaderSettingRepository headerSettingRepository;

    @Transactional(readOnly = true)
    public HeaderSetting getHeaderSetting(User user) {
        return headerSettingRepository.findByUser(user)
                .orElseGet(() -> HeaderSetting.builder().user(user).showTimer(false).showCredit(true).build());
    }

    @Transactional(readOnly = true)
    public HeaderSetting getHeaderSetting(Long userId) {
        return getHeaderSetting(User.builder().id(userId).build());
    }

    @Transactional
    public void updateHeaderSetting(Long userId, boolean showTimer, boolean showCredit) {
        User user = User.builder().id(userId).build();
        HeaderSetting setting = headerSettingRepository.findByUser(user)
                .orElse(HeaderSetting.builder().user(user).build());

        setting.setShowTimer(showTimer);
        setting.setShowCredit(showCredit);

        headerSettingRepository.save(setting);
    }

    @Transactional
    public void initializeDefaultHeader(User user) {
        if (headerSettingRepository.findByUser(user).isPresent()) {
            return;
        }
        HeaderSetting setting = HeaderSetting.builder()
                .user(user)
                .showTimer(false)
                .showCredit(true)
                .build();
        headerSettingRepository.save(setting);
    }
}
