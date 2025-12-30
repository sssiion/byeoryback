package com.project.byeoryback.domain.widget.service;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import com.project.byeoryback.domain.widget.dto.WidgetRequest;
import com.project.byeoryback.domain.widget.entity.Widget;
import com.project.byeoryback.domain.widget.repository.WidgetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository widgetRepository;
    private final UserRepository userRepository;

    // 1. 위젯 생성
    @Transactional
    public Widget createWidget(Long userId, WidgetRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Widget widget = Widget.builder()
                .name(request.getName())
                .type(request.getType())
                .content(request.getContent())
                .styles(request.getStyles())
                .user(user)
                .isShared(false) // 기본은 비공개
                .build();

        return widgetRepository.save(widget);
    }

    // 2. 내 위젯 목록 조회
    public Page<Widget> getMyWidgets(Long userId, Pageable pageable) {
        return widgetRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    // 3. 위젯 마켓(공유됨) 목록 조회
    public Page<Widget> getSharedWidgets(Pageable pageable) {
        return widgetRepository.findAllByIsSharedTrueOrderByCreatedAtDesc(pageable);
    }

    // 4. 위젯 상세 조회 (단건) -> Controller의 getWidget 매칭
    public Widget getWidget(Long id) {
        return widgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("위젯이 존재하지 않습니다. id=" + id));
    }

    // 5. 위젯 수정 -> Controller의 updateWidget 매칭
    @Transactional
    public Widget updateWidget(Long id, WidgetRequest request) {
        Widget widget = widgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("위젯이 존재하지 않습니다. id=" + id));

        // (선택) 여기서 userId 체크 로직을 넣어서 본인 것만 수정하게 막을 수 있음

        widget.update(
                request.getName(),
                request.getContent(),
                request.getStyles(),
                widget.isShared() // 공유 상태는 유지 (별도 API로 변경)
        );
        return widget;
    }

    // 6. 위젯 삭제 -> Controller의 deleteWidget 매칭
    @Transactional
    public void deleteWidget(Long id) {
        // (선택) 본인 확인 로직 필요
        widgetRepository.deleteById(id);
    }

    // 7. 공유 상태 토글 -> Controller의 toggleShare 매칭
    @Transactional
    public Widget toggleShare(Long userId, Long widgetId) {
        Widget widget = widgetRepository.findById(widgetId)
                .orElseThrow(() -> new IllegalArgumentException("위젯이 존재하지 않습니다."));

        // 본인 확인
        if (!widget.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 위젯만 공유 설정을 변경할 수 있습니다.");
        }

        // 상태 반전 및 업데이트 (나머지 필드는 그대로 유지)
        widget.update(
                widget.getName(),
                widget.getContent(),
                widget.getStyles(),
                !widget.isShared() // true <-> false 토글
        );

        return widget;
    }

    // 8. 위젯 가져오기 (Fork) -> Controller의 forkWidget 매칭
    @Transactional
    public Widget forkWidget(Long myUserId, Long originalWidgetId) {
        User me = userRepository.findById(myUserId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Widget originalWidget = widgetRepository.findById(originalWidgetId)
                .orElseThrow(() -> new IllegalArgumentException("원본 위젯이 존재하지 않습니다."));

        if (!originalWidget.isShared()) {
            throw new IllegalArgumentException("공유되지 않은 위젯은 가져올 수 없습니다.");
        }

        // 다운로드 수 증가
        originalWidget.incrementDownloadCount();

        // 복사본 생성 (주인은 '나')
        Widget myCopy = Widget.builder()
                .name(originalWidget.getName())
                .type(originalWidget.getType())
                .content(originalWidget.getContent()) // 데이터 복사
                .styles(originalWidget.getStyles())   // 스타일 복사
                .user(me)                             // 소유자 변경
                .isShared(false)                      // 가져온 건 비공개 시작
                .build();

        return widgetRepository.save(myCopy);
    }
}