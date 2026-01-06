package com.project.byeoryback.domain.room.service;

import com.project.byeoryback.domain.hashtag.entity.Hashtag;
import com.project.byeoryback.domain.hashtag.service.HashtagService;
import com.project.byeoryback.domain.room.dto.RoomRequest;
import com.project.byeoryback.domain.room.dto.RoomResponse;
import com.project.byeoryback.domain.room.entity.Room;
import com.project.byeoryback.domain.room.entity.RoomMember;
import com.project.byeoryback.domain.room.repository.RoomMemberRepository;
import com.project.byeoryback.domain.room.repository.RoomRepository;
import com.project.byeoryback.domain.post.repository.PostRepository;
import com.project.byeoryback.domain.post.dto.PostResponse;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final HashtagService hashtagService;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public RoomResponse createRoom(User user, RoomRequest request) {
        Hashtag hashtag = null;
        if (request.getTagName() != null && !request.getTagName().isEmpty()) {
            hashtag = hashtagService.findOrCreate(request.getTagName());
        }

        String encodedPassword = null;
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .password(encodedPassword)
                .coverImage(request.getCoverImage())
                .coverConfig(request.getCoverConfig())
                .hashtag(hashtag)
                .owner(user)
                .build();

        Room savedRoom = roomRepository.save(room);

        // Add owner as member
        RoomMember member = RoomMember.builder()
                .room(savedRoom)
                .user(user)
                .role(RoomMember.RoomRole.OWNER)
                .build();
        roomMemberRepository.save(member);

        savedRoom.getMembers().add(member);

        return RoomResponse.from(savedRoom);
    }

    @Transactional
    public void joinRoom(Long roomId, User user, String password) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (roomMemberRepository.existsByRoomIdAndUserId(roomId, user.getId())) {
            throw new IllegalArgumentException("Already joined");
        }

        if (room.getPassword() != null && !room.getPassword().isEmpty()) {
            if (password == null || !passwordEncoder.matches(password, room.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }
        }

        RoomMember member = RoomMember.builder()
                .room(room)
                .user(user)
                .role(RoomMember.RoomRole.MEMBER)
                .build();
        roomMemberRepository.save(member);
    }

    public RoomResponse getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return RoomResponse.from(room);
    }

    public List<com.project.byeoryback.domain.room.dto.RoomMemberResponse> getMembers(Long roomId) {
        return roomMemberRepository.findAllByRoomId(roomId).stream()
                .map(member -> {
                    String nickname = userProfileRepository.findByUserId(member.getUser().getId())
                            .map(profile -> profile.getNickname())
                            .orElse(member.getUser().getEmail().split("@")[0]);
                    return com.project.byeoryback.domain.room.dto.RoomMemberResponse.from(member, nickname);
                })
                .toList();
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomResponse::from)
                .toList();
    }

    public List<PostResponse> getRoomPosts(Long roomId) {
        return postRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId).stream()
                .map(PostResponse::from)
                .toList();
    }

    public List<RoomResponse> getMyJoinedRooms(User user) {
        return roomMemberRepository.findAllByUserId(user.getId()).stream()
                .map(member -> RoomResponse.from(member.getRoom()))
                .toList();
    }

    @Transactional
    public void leaveRoom(Long roomId, User user) {
        RoomMember member = roomMemberRepository.findByRoomIdAndUserId(roomId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not a member of this room"));

        if (member.getRole() == RoomMember.RoomRole.OWNER) {
            throw new IllegalArgumentException("Owner cannot leave the room. Please delete the room instead.");
        }

        roomMemberRepository.delete(member);
    }

    @Transactional
    public void kickMember(Long roomId, Long targetUserId, User requester) {
        RoomMember requesterMember = roomMemberRepository.findByRoomIdAndUserId(roomId, requester.getId())
                .orElseThrow(() -> new IllegalArgumentException("You are not a member of this room"));

        if (requesterMember.getRole() != RoomMember.RoomRole.OWNER) {
            throw new IllegalArgumentException("Only the owner can kick members");
        }

        RoomMember targetMember = roomMemberRepository.findByRoomIdAndUserId(roomId, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target user is not a member of this room"));

        if (targetMember.getRole() == RoomMember.RoomRole.OWNER) {
            throw new IllegalArgumentException("Cannot kick the owner");
        }

        roomMemberRepository.delete(targetMember);
    }

    @Transactional
    public void deleteRoom(Long roomId, User requester) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("Only the owner can delete the room");
        }

        roomRepository.delete(room);
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, User requester, RoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("Only the owner can update the room");
        }

        Hashtag hashtag = null;
        if (request.getTagName() != null && !request.getTagName().isEmpty()) {
            hashtag = hashtagService.findOrCreate(request.getTagName());
        }

        String password = null;
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            password = passwordEncoder.encode(request.getPassword());
        }

        room.update(request.getName(), request.getDescription(), password, request.getCoverImage(), hashtag,
                request.getCoverConfig());

        return RoomResponse.from(room);
    }
}
