package com.project.byeoryback.domain.room.service;

import com.project.byeoryback.domain.hashtag.entity.Hashtag;
import com.project.byeoryback.domain.hashtag.service.HashtagService;
import com.project.byeoryback.domain.room.dto.RoomRequest;
import com.project.byeoryback.domain.room.dto.RoomResponse;
import com.project.byeoryback.domain.room.entity.Room;
import com.project.byeoryback.domain.room.entity.RoomMember;
import com.project.byeoryback.domain.room.repository.RoomMemberRepository;
import com.project.byeoryback.domain.room.repository.RoomRepository;
import com.project.byeoryback.domain.user.entity.User;
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
                .map(com.project.byeoryback.domain.room.dto.RoomMemberResponse::from)
                .toList();
    }
}
