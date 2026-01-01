package com.project.byeoryback.domain.pin.service;

import com.project.byeoryback.domain.pin.dto.PinRegisterRequest;
import com.project.byeoryback.domain.pin.dto.PinUnlockRequest;
import com.project.byeoryback.domain.pin.dto.PinVerifyRequest;
import com.project.byeoryback.domain.pin.entity.Pin;
import com.project.byeoryback.domain.auth.service.MailService;
import com.project.byeoryback.domain.pin.repository.PinRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PinServiceTest {

    @Mock
    private PinRepository pinRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @InjectMocks
    private PinService pinService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("PIN 등록 성공 테스트 - 기존 핀 없을 때")
    void registerPin_New() {
        // Given
        Long userId = 1L;
        PinRegisterRequest mockRequest = mock(PinRegisterRequest.class);
        when(mockRequest.getPin()).thenReturn("123456");

        User user = User.builder().id(userId).email("test@test.com").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashed_123456");

        // When
        pinService.registerPin(userId, mockRequest);

        // Then
        verify(pinRepository).save(any(Pin.class));
    }

    @Test
    @DisplayName("PIN 검증 성공 테스트")
    void verifyPin_Success() {
        // Given
        Long userId = 1L;
        PinVerifyRequest mockRequest = mock(PinVerifyRequest.class);
        when(mockRequest.getPin()).thenReturn("123456");

        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).pinHash("hashed_123456").failureCount(0).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));
        when(passwordEncoder.matches("123456", "hashed_123456")).thenReturn(true);

        // When
        boolean result = pinService.verifyPin(userId, mockRequest);

        // Then
        assertTrue(result);
        assertEquals(0, pin.getFailureCount()); // Should reset count
    }

    @Test
    @DisplayName("PIN 검증 실패 시 잠금 테스트")
    void verifyPin_Lockout() {
        // Given
        Long userId = 1L;
        PinVerifyRequest mockRequest = mock(PinVerifyRequest.class);
        when(mockRequest.getPin()).thenReturn("000000");

        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).pinHash("hashed_123456").failureCount(4).build(); // 4 failures already

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));
        when(passwordEncoder.matches("000000", "hashed_123456")).thenReturn(false);

        // When
        boolean result = pinService.verifyPin(userId, mockRequest);

        // Then
        assertFalse(result);
        assertEquals(5, pin.getFailureCount());
        assertTrue(pin.isLocked());
    }

    @Test
    @DisplayName("PIN 잠금 시 검증 불가 테스트")
    void verifyPin_AlreadyLocked() {
        // Given
        Long userId = 1L;
        PinVerifyRequest mockRequest = mock(PinVerifyRequest.class);

        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).pinHash("hashed_123456").failureCount(5).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> pinService.verifyPin(userId, mockRequest));
    }

    @Test
    @DisplayName("PIN 잠금 해제 요청 테스트")
    void requestUnlock_Success() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).failureCount(5).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));

        // When
        pinService.requestUnlock(userId);

        // Then
        try {
            verify(mailService).sendSimpleMessage("test@test.com");
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    @DisplayName("PIN 잠금 해제 성공 테스트")
    void unlockPin_Success() {
        // Given
        Long userId = 1L;
        String code = "123456";
        PinUnlockRequest mockRequest = mock(PinUnlockRequest.class);
        when(mockRequest.getCode()).thenReturn(code);

        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).failureCount(5).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));
        when(mailService.verifyEmailCode("test@test.com", code)).thenReturn(true);

        // When
        pinService.unlockPin(userId, mockRequest);

        // Then
        verify(pinRepository).delete(pin);
    }

    @Test
    @DisplayName("PIN 검증 실패 테스트")
    void verifyPin_Failure() {
        // Given
        Long userId = 1L;
        PinVerifyRequest mockRequest = mock(PinVerifyRequest.class);
        when(mockRequest.getPin()).thenReturn("000000");

        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).pinHash("hashed_123456").failureCount(0).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));
        when(passwordEncoder.matches("000000", "hashed_123456")).thenReturn(false);

        // When
        boolean result = pinService.verifyPin(userId, mockRequest);

        // Then
        assertFalse(result);
        assertEquals(1, pin.getFailureCount()); // Should increment count
    }

    @Test
    @DisplayName("PIN 삭제 테스트")
    void deletePin_Success() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).email("test@test.com").build();
        Pin pin = Pin.builder().user(user).pinHash("hashed_123456").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));

        // When
        pinService.deletePin(userId);

        // Then
        verify(pinRepository).delete(pin);
    }

    @Test
    @DisplayName("PIN 상태 조회 테스트 - 등록됨")
    void getPinStatus_Registered() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        Pin pin = Pin.builder().user(user).failureCount(3).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.of(pin));

        // When
        var status = pinService.getPinStatus(userId);

        // Then
        assertTrue(status.isRegistered());
        assertEquals(3, status.getFailureCount());
        assertFalse(status.isLocked());
    }

    @Test
    @DisplayName("PIN 상태 조회 테스트 - 등록 안 됨")
    void getPinStatus_NotRegistered() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pinRepository.findByUser(user)).thenReturn(Optional.empty());

        // When
        var status = pinService.getPinStatus(userId);

        // Then
        assertFalse(status.isRegistered());
        assertEquals(0, status.getFailureCount());
        assertFalse(status.isLocked());
    }
}
