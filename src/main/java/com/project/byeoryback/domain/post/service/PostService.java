package com.project.byeoryback.domain.post.service;

import com.project.byeoryback.domain.post.dto.PostRequest;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.repository.PostRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.hashtag.service.HashtagService;
import com.project.byeoryback.domain.album.service.AlbumService;
import com.project.byeoryback.domain.album.entity.AlbumContent;
import com.project.byeoryback.domain.album.entity.Album;
import com.project.byeoryback.domain.album.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagService hashtagService;
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    // [추가됨] 커뮤니티 서비스 주입
    private final com.project.byeoryback.domain.community.service.CommunityService communityService;

    // 1. 전체 조회 (최신순)
    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // [New] 특정 유저의 게시글만 조회
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    // 2. 게시글 생성
    @Transactional
    public Post createPost(User user, PostRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .titleStyles(request.getTitleStyles())
                .mode(request.getMode())
                .blocks(request.getBlocks())
                .stickers(request.getStickers())
                .floatingTexts(request.getFloatingTexts())
                .floatingImages(request.getFloatingImages())
                .user(user)
                .isFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false)
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : true) // [체크] 빌더에 isPublic 추가 필요
                .build();

        Post savedPost = postRepository.save(post);

        // Hashtag processing
        hashtagService.processHashtags(savedPost, request.getTags());

        // Album assignment: Always trust Frontend's explicit IDs.
        // Frontend calculates proper album IDs for both MANUAL and AUTO modes.
        // Backend purely persists these associations without hidden auto-logic.
        if (request.getTargetAlbumIds() != null && !request.getTargetAlbumIds().isEmpty()) {
            for (Long albumId : request.getTargetAlbumIds()) {
                albumService.addContentToAlbum(albumId, savedPost.getId(), AlbumContent.ContentType.POST);
            }
        }
        // Folder assignment logic can be added similarly if needed

        return savedPost;
    }

    // 3. 게시글 수정
    @Transactional
    public Post updatePost(User user, Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        post.update(
                request.getTitle(),
                request.getTitleStyles(),
                request.getBlocks(),
                request.getStickers(),
                request.getFloatingTexts(),
                request.getFloatingImages(),
                request.getIsFavorite(),
                request.getMode(),
                request.getIsPublic());

        hashtagService.processHashtags(post, request.getTags());

        // [수정됨] 앨범 연결 관계 재설정 (Replace Logic)
        // 1. 기존의 모든 앨범 연결 제거
        post.getAlbumContents().clear();

        // 2. 새 앨범 목록 연결
        if (request.getTargetAlbumIds() != null && !request.getTargetAlbumIds().isEmpty()) {
            List<Album> targetAlbums = albumRepository.findAllById(request.getTargetAlbumIds());

            for (Album album : targetAlbums) {
                AlbumContent content = AlbumContent.builder()
                        .parentAlbum(album)
                        .childPost(post)
                        .contentType(AlbumContent.ContentType.POST)
                        .build();
                post.getAlbumContents().add(content);
            }
        }

        return post;
    }

    // 4. 삭제
    @Transactional
    public void deletePost(Long id) {
        communityService.deleteByPostId(id);
        postRepository.deleteById(id);
    }

    // 5. 월별 게시글 요약 조회
    public List<com.project.byeoryback.domain.post.dto.PostSummaryResponse> getPostsSummaryByYearMonth(Long userId,
            int year, int month) {
        java.time.LocalDateTime start = java.time.LocalDateTime.of(year, month, 1, 0, 0, 0);
        java.time.LocalDateTime end = start.plusMonths(1).minusNanos(1); // End of the month

        List<Post> posts = postRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
        return posts.stream()
                .map(com.project.byeoryback.domain.post.dto.PostSummaryResponse::from)
                .toList();
    }

}