package com.project.byeoryback.domain.widget.repository;


import com.project.byeoryback.domain.widget.entity.WidgetDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WidgetDefinitionRepository extends JpaRepository<WidgetDefinition, Long> {
    // 시스템 위젯과 특정 유저의 커스텀 위젯을 함께 조회
    List<WidgetDefinition> findByIsSystemTrueOrUserId(Long userId);

    // 비로그인 시 시스템 위젯만 조회
    List<WidgetDefinition> findByIsSystemTrue();

    Optional<WidgetDefinition> findByWidgetType(String widgetType);
}