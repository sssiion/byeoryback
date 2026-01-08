package com.project.byeoryback.domain.room.service;

import com.project.byeoryback.domain.room.dto.RoomCycleCreateRequest;
import com.project.byeoryback.domain.room.dto.RoomCycleResponse;
import com.project.byeoryback.domain.room.entity.Room;
import com.project.byeoryback.domain.room.entity.RoomCycle;
import com.project.byeoryback.domain.room.entity.RoomCycleMember;
import com.project.byeoryback.domain.room.repository.RoomCycleMemberRepository;
import com.project.byeoryback.domain.room.repository.RoomCycleRepository;
import com.project.byeoryback.domain.room.repository.RoomRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoomCycleServiceTest {

    @InjectMocks
    private RoomCycleService roomCycleService;

    @Mock
    private RoomCycleRepository roomCycleRepository;
    @Mock
    private RoomCycleMemberRepository roomCycleMemberRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private com.project.byeoryback.domain.user.repository.UserProfileRepository userProfileRepository;
    @Mock
    private com.project.byeoryback.domain.post.service.PostService postService;
    @Mock
    private com.project.byeoryback.domain.post.repository.PostRepository postRepository;

    @Test
    @DisplayName("Create Cycle - Success")
    void createCycle() {
        // Given
        Long roomId = 1L;
        Long ownerId = 100L;
        User owner = User.builder().id(ownerId).email("owner@test.com").build();
        Room room = Room.builder().owner(owner).build();
        ReflectionTestUtils.setField(room, "id", roomId);

        Long memberId1 = 101L;
        Long memberId2 = 102L;
        User user1 = User.builder().id(memberId1).email("user1@test.com").build();
        User user2 = User.builder().id(memberId2).email("user2@test.com").build();

        RoomCycleCreateRequest request = RoomCycleCreateRequest.builder()
                .title("Test Cycle")
                .type(RoomCycle.CycleType.EXCHANGE_DIARY)
                .targetMemberIds(List.of(memberId1, memberId2))
                .timeLimitHours(24)
                .build();

        given(roomRepository.findById(roomId)).willReturn(Optional.of(room));
        given(userRepository.findById(memberId1)).willReturn(Optional.of(user1));
        given(userRepository.findById(memberId2)).willReturn(Optional.of(user2));
        given(roomCycleRepository.save(any(RoomCycle.class))).willAnswer(invocation -> {
            RoomCycle cycle = invocation.getArgument(0);
            ReflectionTestUtils.setField(cycle, "id", 1L);
            // Ensure members list is initialized if it's null (Builder side effect)
            if (cycle.getMembers() == null) {
                ReflectionTestUtils.setField(cycle, "members", new ArrayList<>());
            }
            return cycle;
        });

        // When
        RoomCycleResponse response = roomCycleService.createCycle(roomId, owner, request);

        // Then
        assertThat(response.getTitle()).isEqualTo("Test Cycle");
        assertThat(response.getStatus()).isEqualTo(RoomCycle.CycleStatus.IN_PROGRESS);
        assertThat(response.getMembers()).hasSize(2);
        assertThat(response.getMembers().get(0).getUserId()).isEqualTo(memberId1);
        assertThat(response.getMembers().get(0).getOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("Pass Turn - Next User")
    void passTurn_Next() {
        // Given
        Long cycleId = 1L;
        Long userId1 = 101L;
        Long userId2 = 102L;
        User user1 = User.builder().id(userId1).email("user1@test.com").build();

        Room room = Room.builder().id(1L).build();
        RoomCycle cycle = RoomCycle.builder()
                .room(room)
                .status(RoomCycle.CycleStatus.IN_PROGRESS)
                .currentTurnOrder(1)
                .members(new ArrayList<>())
                .build();
        ReflectionTestUtils.setField(cycle, "id", cycleId);

        RoomCycleMember member1 = RoomCycleMember.builder().user(user1).turnOrder(1).isCompleted(false).build();
        RoomCycleMember member2 = RoomCycleMember.builder()
                .user(User.builder().id(userId2).email("user2@test.com").build()).turnOrder(2)
                .isCompleted(false).build();
        cycle.addMember(member1);
        cycle.addMember(member2);

        given(roomCycleRepository.findById(cycleId)).willReturn(Optional.of(cycle));
        given(roomCycleMemberRepository.findByCycleIdAndUserId(cycleId, userId1)).willReturn(Optional.of(member1));

        // When
        // When
        RoomCycleResponse response = roomCycleService.passTurn(cycleId, user1);

        // Then
        assertThat(member1.isCompleted()).isTrue();
        assertThat(response.getCurrentTurnOrder()).isEqualTo(2);
        assertThat(response.getStatus()).isEqualTo(RoomCycle.CycleStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Pass Turn - Complete Cycle")
    void passTurn_Complete() {
        // Given
        Long cycleId = 1L;
        Long userId1 = 101L;
        User user1 = User.builder().id(userId1).email("user1@test.com").build();

        Room room = Room.builder().id(1L).build();
        RoomCycle cycle = RoomCycle.builder()
                .room(room)
                .status(RoomCycle.CycleStatus.IN_PROGRESS)
                .currentTurnOrder(1)
                .members(new ArrayList<>())
                .build();

        RoomCycleMember member1 = RoomCycleMember.builder().user(user1).turnOrder(1).isCompleted(false).build();
        cycle.addMember(member1);

        given(roomCycleRepository.findById(cycleId)).willReturn(Optional.of(cycle));
        given(roomCycleMemberRepository.findByCycleIdAndUserId(cycleId, userId1)).willReturn(Optional.of(member1));

        // When
        // When
        RoomCycleResponse response = roomCycleService.passTurn(cycleId, user1);

        // Then
        assertThat(member1.isCompleted()).isTrue();
        assertThat(response.getStatus()).isEqualTo(RoomCycle.CycleStatus.COMPLETED);
    }
}
