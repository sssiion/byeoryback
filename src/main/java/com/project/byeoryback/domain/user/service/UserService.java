package com.project.byeoryback.domain.user.service;

import com.project.byeoryback.domain.user.dto.UserProfileRequest;
import com.project.byeoryback.domain.user.dto.UserProfileResponse;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.entity.UserProfile;
import com.project.byeoryback.domain.user.repository.UserProfileRepository;
import com.project.byeoryback.domain.user.repository.UserRepository;
import com.project.byeoryback.domain.user.exception.UserProfileNotFoundException;

import com.project.byeoryback.domain.user.exception.NicknameAlreadyExistsException;
import com.project.byeoryback.domain.post.repository.PostRepository;
import com.project.byeoryback.domain.todo.repository.TodoRepository;
import com.project.byeoryback.domain.setting.page.repository.PageSettingRepository;
import com.project.byeoryback.domain.setting.theme.repository.ThemeSettingRepository;
import com.project.byeoryback.domain.setting.menu.repository.MenuSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

        private final UserRepository userRepository;
        private final UserProfileRepository userProfileRepository;
        private final PostRepository postRepository;
        private final TodoRepository todoRepository;
        private final PageSettingRepository pageSettingRepository;
        private final ThemeSettingRepository themeSettingRepository;
        private final MenuSettingRepository menuSettingRepository;

        @Transactional(readOnly = true)
        public UserProfileResponse getUserProfile(Long userId) {
                UserProfile profile = userProfileRepository.findByUserId(userId)
                                .orElseThrow(() -> new UserProfileNotFoundException(userId));

                return new UserProfileResponse(
                                profile.getProfilePhoto(),
                                profile.getName(),
                                profile.getNickname(),
                                profile.getUser().getEmail(),
                                profile.getBirthDate(),
                                profile.getPhone(),
                                profile.getGender(),
                                profile.getBio());
        }

        @Transactional
        public void updateUserProfile(Long userId, UserProfileRequest request) {
                UserProfile profile = userProfileRepository.findByUserId(userId)
                                .orElseThrow(() -> new UserProfileNotFoundException(userId));

                if (!profile.getNickname().equals(request.nickname())
                                && userProfileRepository.existsByNickname(request.nickname())) {
                        throw new NicknameAlreadyExistsException("Nickname already exists: " + request.nickname());
                }

                profile.update(
                                request.profilePhoto(),
                                request.name(),
                                request.nickname(),
                                request.birthDate(),
                                request.phone(),
                                request.gender(),
                                request.bio());
        }

        @Transactional
        public void completeUserProfile(Long userId, UserProfileRequest request) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                if (user.isFullProfile()) {
                        throw new IllegalStateException("User profile is already completed");
                }

                if (userProfileRepository.existsByNickname(request.nickname())) {
                        throw new NicknameAlreadyExistsException("Nickname already exists: " + request.nickname());
                }

                UserProfile userProfile = UserProfile.builder()
                                .user(user)
                                .profilePhoto(request.profilePhoto())
                                .name(request.name())
                                .nickname(request.nickname())
                                .birthDate(request.birthDate())
                                .phone(request.phone())
                                .gender(request.gender())
                                .bio(request.bio())
                                .build();

                userProfileRepository.save(userProfile);

                user.completeProfile();
                // Transactional will dirty check and save user update, but explicitly saving if
                // needed
                // userRepository.save(user);
        }

        @Transactional
        public void deleteUser(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // 1. Delete User Profile
                // Note: If UserProfile has cascade delete on User, this might be optional,
                // but checking entity definition usually it's cleaner to delete children first
                // or rely on constraints.
                // Assuming explicit deletion for safety/clarity.
                userProfileRepository.findByUserId(userId).ifPresent(userProfileRepository::delete);

                // 2. Delete Posts
                postRepository.deleteByUserId(userId);

                // 3. Delete Todos
                todoRepository.deleteByUser(user);

                // 4. Delete Settings
                pageSettingRepository.deleteByUserId(userId);
                themeSettingRepository.deleteByUserId(userId);
                menuSettingRepository.deleteByUserId(userId);

                // 5. Delete User
                userRepository.delete(user);
        }
}
