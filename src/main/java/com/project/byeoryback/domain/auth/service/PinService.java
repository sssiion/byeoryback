package com.project.byeoryback.domain.auth.service;

import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public void setupPin(User user, String pin) {
        // Validate PIN format (4 digits)
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be 4 digits");
        }

        user.setPinCode(passwordEncoder.encode(pin));
        user.enablePin();
        user.resetPinFailedAttempts();
        user.unlockPin(); // Ensure it's not locked when setting up new
        userRepository.save(user);
    }

    @Transactional
    public void togglePin(User user, boolean enable) {
        if (enable) {
            if (user.getPinCode() == null) {
                throw new IllegalStateException("Cannot enable PIN without setting it up first");
            }
            user.enablePin();
        } else {
            user.disablePin();
        }
        userRepository.save(user);
    }

    @Transactional
    public boolean verifyPin(User user, String rawPin) {
        if (!user.isPinEnabled()) {
            throw new IllegalStateException("PIN is not enabled for this user");
        }

        if (user.isPinLocked()) {
            throw new IllegalStateException(
                    "Account is locked due to too many failed PIN attempts. Please use email verification.");
        }

        if (passwordEncoder.matches(rawPin, user.getPinCode())) {
            user.resetPinFailedAttempts();
            userRepository.save(user);
            return true;
        } else {
            user.incrementPinFailedAttempts();
            if (user.getPinFailedAttempts() >= 5) {
                lockAccount(user);
            }
            userRepository.save(user);
            return false;
        }
    }

    private void lockAccount(User user) {
        String code = generateVerifyCode();
        user.lockPin(code);
        userRepository.save(user);

        // Send email
        mailService.sendPinCode(user.getEmail(), code);
    }

    @Transactional
    public void unlockWithCode(User user, String code) {
        if (!user.isPinLocked()) {
            return; // Already unlocked
        }

        if (code.equals(user.getPinUnlockCode())) {
            user.unlockPin();
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid verification code");
        }
    }

    @Transactional
    public void resendUnlockCode(User user) {
        if (!user.isPinLocked()) {
            throw new IllegalStateException("Account is not locked");
        }

        String code = generateVerifyCode(); // Regenerate or reuse? Let's regenerate for security.
        user.lockPin(code); // Update code
        userRepository.save(user);

        mailService.sendPinCode(user.getEmail(), code);
    }

    private String generateVerifyCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
