package com.project.byeoryback.global.exception;

import com.project.byeoryback.domain.auth.exception.EmailAlreadyExistsException;
import com.project.byeoryback.domain.auth.exception.SocialAccountException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(com.project.byeoryback.domain.user.exception.UserProfileNotFoundException.class)
    public ResponseEntity<Void> handleUserProfileNotFoundException(
            com.project.byeoryback.domain.user.exception.UserProfileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(com.project.byeoryback.domain.user.exception.NicknameAlreadyExistsException.class)
    public ResponseEntity<String> handleNicknameAlreadyExistsException(
            com.project.byeoryback.domain.user.exception.NicknameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(com.project.byeoryback.domain.user.exception.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(
            com.project.byeoryback.domain.user.exception.UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(com.project.byeoryback.domain.auth.exception.InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(
            com.project.byeoryback.domain.auth.exception.InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(SocialAccountException.class)
    public ResponseEntity<Map<String, String>> handleSocialAccountException(SocialAccountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("code", "SOCIAL_ACCOUNT", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
