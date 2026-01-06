package com.project.byeoryback.domain.community.repository;


import com.project.byeoryback.domain.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    // 게시글 ID로 커뮤니티 글 찾기 (동기화 로직 필수)
    Optional<Community> findByPostId(Long postId);

    // 유저 탈퇴 시 삭제용
    void deleteByUserId(Long userId);

    // 게시글 삭제 시 커뮤니티 글 삭제용
    void deleteByPostId(Long postId);
}