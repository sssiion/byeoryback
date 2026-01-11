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
    private final com.project.byeoryback.domain.setting.widget.repository.WidgetPresetRepository widgetPresetRepository;

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

        // 1. Welcome (Top, Full Width) [x:1, y:1]
        layout.add(createWidgetItem("welcome", 1, 1, 4, 1));

        // 2. Todo List (Left, Large) [x:1, y:2]
        layout.add(createWidgetItem("todo-list", 1, 2, 2, 2));

        // 3. Ocean Wave (Right, Top) [x:3, y:2]
        layout.add(createWidgetItem("ocean-wave", 3, 2, 2, 1));

        // 4. Physics Box (Right, Bottom Left) [x:3, y:3]
        layout.add(createWidgetItem("physics-box", 3, 3, 1, 1));

        // 5. Scratch Card (Right, Bottom Right) [x:4, y:3]
        layout.add(createWidgetItem("scratch-card", 4, 3, 1, 1));

        return layout;
    }

    private Map<String, Object> createWidgetItem(String type, int x, int y, int w, int h) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", type + "-" + System.currentTimeMillis() + "-" + Math.random()); // Unique ID
        item.put("type", type);

        Map<String, Object> layout = new HashMap<>();
        layout.put("x", x);
        layout.put("y", y);
        layout.put("w", w);
        layout.put("h", h);
        item.put("layout", layout);

        item.put("props", new HashMap<>());
        return item;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getWidgets(Long userId) {
        return widgetSettingRepository.findByUser(User.builder().id(userId).build())
                .map(WidgetSetting::getLayoutData)
                .orElse(new ArrayList<>());
    }

    @Transactional
    public void updateWidgets(Long userId, List<Map<String, Object>> layoutData) {
        User user = User.builder().id(userId).build();
        WidgetSetting setting = widgetSettingRepository.findByUser(user)
                .orElse(WidgetSetting.builder().user(user).build());

        setting.updateLayout(layoutData);
        widgetSettingRepository.save(setting);
    }

    // --- Preset Management ---

    @Transactional
    public com.project.byeoryback.domain.setting.widget.entity.WidgetPreset createPreset(Long userId, String name,
            List<Map<String, Object>> layoutData, Map<String, Integer> gridSize) {
        User user = User.builder().id(userId).build();
        com.project.byeoryback.domain.setting.widget.entity.WidgetPreset preset = com.project.byeoryback.domain.setting.widget.entity.WidgetPreset
                .builder()
                .user(user)
                .name(name)
                .layoutData(layoutData)
                .gridSize(gridSize)
                .build();
        return widgetPresetRepository.save(preset);
    }

    @Transactional(readOnly = true)
    public List<com.project.byeoryback.domain.setting.widget.entity.WidgetPreset> getPresets(Long userId) {
        User user = User.builder().id(userId).build();
        return widgetPresetRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void deletePreset(Long userId, Long presetId) {
        // Ensure user owns the preset
        com.project.byeoryback.domain.setting.widget.entity.WidgetPreset preset = widgetPresetRepository
                .findById(presetId)
                .orElseThrow(() -> new IllegalArgumentException("Preset not found"));

        if (!preset.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized to delete this preset");
        }

        widgetPresetRepository.delete(preset);
    }

    @Transactional
    public void updatePreset(Long userId, Long presetId, List<Map<String, Object>> layoutData,
            Map<String, Integer> gridSize) {
        com.project.byeoryback.domain.setting.widget.entity.WidgetPreset preset = widgetPresetRepository
                .findById(presetId)
                .orElseThrow(() -> new IllegalArgumentException("Preset not found"));

        if (!preset.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized to update this preset");
        }

        preset.update(null, layoutData, gridSize);
        widgetPresetRepository.save(preset);
    }
}
