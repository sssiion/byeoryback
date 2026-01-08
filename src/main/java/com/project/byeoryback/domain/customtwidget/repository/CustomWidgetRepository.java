package com.project.byeoryback.domain.customtwidget.repository;

import com.project.byeoryback.domain.customtwidget.entity.CustomWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomWidgetRepository extends JpaRepository<CustomWidget, Long> {

    // 1. 내 위젯 목록 조회 (마이페이지용)
    // SELECT * FROM widgets WHERE user_id = ? ORDER BY created_at DESC
    Page<CustomWidget> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 2. 공유된 위젯 목록 조회 (위젯 마켓용)
    // SELECT * FROM widgets WHERE is_shared = true ORDER BY created_at DESC
    Page<CustomWidget> findAllByIsSharedTrueOrderByCreatedAtDesc(Pageable pageable);

    // (선택) 인기순 조회
    // Page<Widget> findAllByIsSharedTrueOrderByDownloadCountDesc(Pageable pageable);
}