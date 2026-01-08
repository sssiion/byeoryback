package com.project.byeoryback.domain.post.repository;

import com.project.byeoryback.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
        // 특정 유저의 글 목록 조회 (최신순)
        List<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId);

        List<Post> findAllByRoomIdOrderByCreatedAtDesc(Long roomId);

        @org.springframework.data.jpa.repository.Query("SELECT p FROM Post p WHERE p.user.id = :userId " +
                        "AND p.id NOT IN (SELECT ph.post.id FROM PostHashtag ph) " +
                        "AND p.id NOT IN (SELECT ac.childPost.id FROM AlbumContent ac)")
        List<Post> findUnclassifiedPosts(Long userId);

        void deleteByUserId(Long userId);

        org.springframework.data.domain.Page<Post> findByIsPublicTrue(
                        org.springframework.data.domain.Pageable pageable);

        // [New] 해시태그로 공개 게시글 검색
        @org.springframework.data.jpa.repository.Query("SELECT p FROM Post p " +
                        "JOIN p.postHashtags ph " +
                        "JOIN ph.hashtag h " +
                        "WHERE p.isPublic = true AND h.name = :hashtag")
        org.springframework.data.domain.Page<Post> findByIsPublicTrueAndHashtag(
                        @Param("hashtag") String hashtag,
                        org.springframework.data.domain.Pageable pageable);

        // [New] 기간별(월별) 내 게시글 조회
        List<Post> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, java.time.LocalDateTime start,
                        java.time.LocalDateTime end);
}