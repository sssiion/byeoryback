package com.project.byeoryback.domain.setting.widget;

import com.project.byeoryback.domain.setting.widget.entity.WidgetSetting;
import com.project.byeoryback.domain.setting.widget.repository.WidgetSettingRepository;
import com.project.byeoryback.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetSettingRepository widgetSettingRepository;

    @Transactional
    public void initializeDefaultWidgets(User user) {
        if (widgetSettingRepository.findByUser(user).isPresent()) {
            return;
        }

        List<Map<String, Object>> defaultLayout = createDefaultLayout();

        WidgetSetting widgetSetting = WidgetSetting.builder()
                .user(user)
                .layoutData(defaultLayout)
                .build();

        widgetSettingRepository.save(widgetSetting);
    }

    private List<Map<String, Object>> createDefaultLayout() {
        List<Map<String, Object>> layout = new ArrayList<>();

        // 1. Welcome Widget (2x1)
        layout.add(createWidgetItem("welcome", 0, 0, 2, 1));

        // 2. Daily Diary (2x2)
        layout.add(createWidgetItem("daily-diary", 0, 1, 2, 2));

        // 3. Todo List (2x2)
        layout.add(createWidgetItem("todo-list", 2, 0, 2, 2));

        // 4. Calendar/D-Day (2x2) - Optional, let's add D-Day
        layout.add(createWidgetItem("dday", 2, 2, 2, 2));

        return layout;
    }

    private Map<String, Object> createWidgetItem(String type, int x, int y, int w, int h) {
        Map<String, Object> item = new HashMap<>();
        item.put("i", type + "-" + System.currentTimeMillis() + "-" + Math.random()); // Unique ID
        item.put("x", x);
        item.put("y", y);
        item.put("w", w);
        item.put("h", h);
        item.put("type", type);
        item.put("props", new HashMap<>());
        return item;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getWidgets(Long userId) {
        return widgetSettingRepository.findByUser(User.builder().id(userId).build())
                .map(WidgetSetting::getLayoutData)
                .orElse(new ArrayList<>());
    }
}
