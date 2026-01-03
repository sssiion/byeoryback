package com.project.byeoryback.domain.post.repository;

import com.project.byeoryback.domain.post.entity.Block;
import com.project.byeoryback.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 유저의 글 목록 조회 (최신순)
    // 특정 유저의 글 목록 조회 (최신순)
    List<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Post p WHERE p.user.id = :userId " +
            "AND p.id NOT IN (SELECT ph.post.id FROM PostHashtag ph) " +
            "AND p.id NOT IN (SELECT ac.childPost.id FROM AlbumContent ac)")
    List<Post> findUnclassifiedPosts(Long userId);

    void deleteByUserId(Long userId);
    // 방법 A: 유저의 모든 게시글을 다 가져오는 기본 메서드
    List<Post> findAllByUserId(Long userId);

    // 방법 B (최적화): 유저 ID로 검색하되, 'blocks' 컬럼만 조회 (Projection)
    // Post 전체가 아니라 List<Block> 데이터만 가져옵니다.
    @Query("SELECT p.blocks FROM Post p WHERE p.user.id = :userId")
    List<List<Block>> findBlocksByUserId(@Param("userId") Long userId);
}