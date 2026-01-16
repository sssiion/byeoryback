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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomCycleService {

    private final RoomCycleRepository roomCycleRepository;
    private final RoomCycleMemberRepository roomCycleMemberRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final com.project.byeoryback.domain.user.repository.UserProfileRepository userProfileRepository;
    private final com.project.byeoryback.domain.post.service.PostService postService;
    private final com.project.byeoryback.domain.post.repository.PostRepository postRepository;

    @Transactional
    public RoomCycleResponse createCycle(Long roomId, User requester, RoomCycleCreateRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("Only the owner can start a new cycle");
        }

        RoomCycle cycle = RoomCycle.builder()
                .room(room)
                .title(request.getTitle())
                .type(request.getType())
                .status(RoomCycle.CycleStatus.IN_PROGRESS)
                .currentTurnOrder(1)
                .nextTurnTime(request.getTimeLimitHours() != null
                        ? LocalDateTime.now().plusHours(request.getTimeLimitHours())
                        : null)
                .build();

        roomCycleRepository.save(cycle);

        int order = 1;
        for (Long memberId : request.getTargetMemberIds()) {
            User memberUser = userRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + memberId));

            RoomCycleMember cycleMember = RoomCycleMember.builder()
                    .user(memberUser)
                    .turnOrder(order++)
                    .isCompleted(false)
                    .build();

            cycle.addMember(cycleMember); // This helps cascade save if configured, but explicit save is safer if not
                                          // fully cascaded
            // roomCycleMemberRepository.save(cycleMember); // Valid if CascadeType.ALL is
            // on OneToMany
        }

        // Ensure members are saved (if cascade is not sufficient, though we put
        // CascadeType.ALL)
        // With CascadeType.ALL in RoomCycle, saving cycle should save members
        // effectively if added to list.

        return RoomCycleResponse.from(cycle, requester.getId(), getNicknamesMap(cycle.getMembers()));
    }

    public List<RoomCycleResponse> getRoomCycles(Long roomId, User requester) {
        return roomCycleRepository.findAllByRoomId(roomId).stream()
                .map(cycle -> RoomCycleResponse.from(cycle, requester.getId(), getNicknamesMap(cycle.getMembers())))
                .toList();
    }

    public RoomCycleResponse getCycle(Long cycleId, User requester) {
        RoomCycle cycle = roomCycleRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found"));
        return RoomCycleResponse.from(cycle, requester.getId(), getNicknamesMap(cycle.getMembers()));
    }

    @Transactional
    public RoomCycleResponse passTurn(Long cycleId, User requester) {
        RoomCycle cycle = roomCycleRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found"));

        if (cycle.getStatus() == RoomCycle.CycleStatus.COMPLETED) {
            throw new IllegalArgumentException("Cycle is already completed");
        }

        RoomCycleMember currentMember = roomCycleMemberRepository.findByCycleIdAndUserId(cycleId, requester.getId())
                .orElseThrow(() -> new IllegalArgumentException("You are not a member of this cycle"));

        if (!currentMember.getTurnOrder().equals(cycle.getCurrentTurnOrder())) {
            throw new IllegalArgumentException("It is not your turn");
        }

        // Complete current member
        currentMember.completeTurn();

        // Check if there is next member
        int nextOrder = cycle.getCurrentTurnOrder() + 1;
        boolean hasNext = cycle.getMembers().stream().anyMatch(m -> m.getTurnOrder() == nextOrder);

        if (hasNext) {
            LocalDateTime nextTime = cycle.getNextTurnTime();
            if (nextTime != null) {
                cycle.nextTurn(LocalDateTime.now().plusHours(24));
            } else {
                cycle.nextTurn(null);
            }
        } else {
            cycle.complete();
        }

        // Return updated state
        return RoomCycleResponse.from(cycle, requester.getId(), getNicknamesMap(cycle.getMembers()));
    }

    private java.util.Map<Long, String> getNicknamesMap(List<RoomCycleMember> members) {
        if (members == null)
            return java.util.Collections.emptyMap();
        List<Long> userIds = members.stream().map(m -> m.getUser().getId()).toList();
        return userProfileRepository.findAllByUserIdIn(userIds).stream()
                .collect(java.util.stream.Collectors.toMap(
                        p -> p.getUser().getId(),
                        p -> p.getNickname()));
    }

    @Transactional
    public com.project.byeoryback.domain.post.dto.PostResponse getCycleContent(Long cycleId, User requester) {
        RoomCycle cycle = roomCycleRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found"));

        if (cycle.getRoom().getMembers().stream().noneMatch(m -> m.getUser().getId().equals(requester.getId()))) {
            throw new IllegalArgumentException("You are not a member of this room");
        }

        if (cycle.getPostId() == null) {
            return null;
        }

        com.project.byeoryback.domain.post.entity.Post post = postRepository.findById(cycle.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return com.project.byeoryback.domain.post.dto.PostResponse.from(post);
    }

    @Transactional
    public com.project.byeoryback.domain.post.dto.PostResponse submitCycleContent(Long cycleId, User requester,
            com.project.byeoryback.domain.post.dto.PostRequest request) {
        RoomCycle cycle = roomCycleRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found"));

        if (cycle.getStatus() == RoomCycle.CycleStatus.COMPLETED) {
            throw new IllegalArgumentException("Cycle is already completed");
        }

        RoomCycleMember currentMember = roomCycleMemberRepository.findByCycleIdAndUserId(cycleId, requester.getId())
                .orElseThrow(() -> new IllegalArgumentException("You are not a member of this cycle"));

        if (!currentMember.getTurnOrder().equals(cycle.getCurrentTurnOrder())) {
            throw new IllegalArgumentException("It is not your turn");
        }

        // [Fix] Enforce Room Scope and Private Visibility
        request.setRoomId(cycle.getRoom().getId());
        request.setIsPublic(false);

        com.project.byeoryback.domain.post.entity.Post post;
        if (cycle.getPostId() == null) {
            post = postService.createPost(requester, request);
            cycle.updatePostId(post.getId());
        } else {
            post = postService.updatePost(requester, cycle.getPostId(), request);
        }

        return com.project.byeoryback.domain.post.dto.PostResponse.from(post);
    }
}
