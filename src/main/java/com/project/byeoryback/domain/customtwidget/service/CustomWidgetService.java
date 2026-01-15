package com.project.byeoryback.domain.customtwidget.service;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import com.project.byeoryback.domain.customtwidget.dto.CustomWidgetRequest;
import com.project.byeoryback.domain.customtwidget.entity.CustomWidget;
import com.project.byeoryback.domain.customtwidget.repository.CustomWidgetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomWidgetService {

    private final CustomWidgetRepository customWidgetRepository;
    private final UserRepository userRepository;

    // 1. 위젯 생성
    @Transactional
    public CustomWidget createWidget(Long userId, CustomWidgetRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        CustomWidget customWidget = CustomWidget.builder()
                .user(user)
                .name(request.getName())
                .type(request.getType())
                .defaultSize(request.getDefaultSize() != null ? request.getDefaultSize() : "2x2") // 🌟 없으면 2x2 기본값
                .content(request.getContent())
                .styles(request.getStyles())
                .decorations(request.getDecorations()) // 🌟 [NEW] 꾸미기 요소 저장
                .isShared(false) // 기본은 비공개
                .build();

        return customWidgetRepository.save(customWidget);
    }

    // 2. 내 위젯 목록 조회
    public Page<CustomWidget> getMyWidgets(Long userId, Pageable pageable) {
        return customWidgetRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    // 3. 위젯 마켓(공유됨) 목록 조회
    public Page<CustomWidget> getSharedWidgets(Pageable pageable) {
        return customWidgetRepository.findAllByIsSharedTrueOrderByCreatedAtDesc(pageable);
    }

    // 4. 위젯 상세 조회 (단건) -> Controller의 getWidget 매칭
    public CustomWidget getWidget(Long id) {
        return customWidgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("위젯이 존재하지 않습니다. id=" + id));
    }

    // 5. 위젯 수정 -> Controller의 updateWidget 매칭
    @Transactional
    public CustomWidget updateWidget(Long widgetId, CustomWidgetRequest request) {
        CustomWidget customWidget = customWidgetRepository.findById(widgetId)
                .orElseThrow(() -> new IllegalArgumentException("Widget not found"));
        // (선택) 여기서 userId 체크 로직을 넣어서 본인 것만 수정하게 막을 수 있음

        customWidget.update(
                request.getName(),
                request.getDefaultSize(),
                request.getContent(),
                request.getStyles(),
                request.getDecorations(), // 🌟 [NEW] 꾸미기 요소 수정
                request.isShared() // DTO에 없다면 false or 기존값 유지
        );
        return customWidget;
    }

    // 6. 위젯 삭제 -> Controller의 deleteWidget 매칭
    @Transactional
    public void deleteWidget(Long id) {
        // (선택) 본인 확인 로직 필요
        customWidgetRepository.deleteById(id);
    }

    // 7. 공유 상태 토글 -> Controller의 toggleShare 매칭
    @Transactional
    public CustomWidget toggleShare(Long userId, Long widgetId) {
        CustomWidget customWidget = customWidgetRepository.findById(widgetId)
                .orElseThrow(() -> new IllegalArgumentException("위젯이 존재하지 않습니다."));

        // 본인 확인
        if (!customWidget.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 위젯만 공유 설정을 변경할 수 있습니다.");
        }

        // 상태 반전 및 업데이트 (나머지 필드는 그대로 유지)
        customWidget.update(
                customWidget.getName(),
                customWidget.getDefaultSize(),
                customWidget.getContent(),
                customWidget.getStyles(),
                customWidget.getDecorations(), // 🌟 [NEW] 꾸미기 요소 유지
                !customWidget.isShared() // true <-> false 토글
        );

        return customWidget;
    }

    // 8. 위젯 가져오기 (Fork) -> Controller의 forkWidget 매칭
    @Transactional
    public CustomWidget forkWidget(Long myUserId, Long originalWidgetId) {
        User me = userRepository.findById(myUserId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        CustomWidget originalCustomWidget = customWidgetRepository.findById(originalWidgetId)
                .orElseThrow(() -> new IllegalArgumentException("원본 위젯이 존재하지 않습니다."));

        if (!originalCustomWidget.isShared()) {
            throw new IllegalArgumentException("공유되지 않은 위젯은 가져올 수 없습니다.");
        }

        // 다운로드 수 증가
        originalCustomWidget.incrementDownloadCount();

        // 복사본 생성 (주인은 '나')
        CustomWidget myCopy = CustomWidget.builder()
                .name(originalCustomWidget.getName())
                .type(originalCustomWidget.getType())
                .content(originalCustomWidget.getContent()) // 데이터 복사
                .styles(originalCustomWidget.getStyles()) // 스타일 복사
                .decorations(originalCustomWidget.getDecorations()) // 🌟 [NEW] 꾸미기 요소 복사
                .user(me) // 소유자 변경
                .isShared(false) // 가져온 건 비공개 시작
                .build();

        return customWidgetRepository.save(myCopy);
    }
}