package com.project.byeoryback.domain.post.service;

import com.project.byeoryback.domain.post.dto.PostRequest;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.repository.PostRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.hashtag.service.HashtagService;
import com.project.byeoryback.domain.album.service.AlbumService;
import com.project.byeoryback.domain.album.entity.AlbumContent;
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

    // 1. 전체 조회 (최신순)
    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // 2. 게시글 생성
    @Transactional
    public Post createPost(User user, PostRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .blocks(request.getBlocks())
                .stickers(request.getStickers())
                .floatingTexts(request.getFloatingTexts())
                .floatingImages(request.getFloatingImages())
                .user(user)
                .isFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false)
                .build();

        Post savedPost = postRepository.save(post);

        // Hashtag processing
        hashtagService.processHashtags(savedPost, request.getHashtags());

        // Manual Album/Folder assignment
        if ("MANUAL".equalsIgnoreCase(request.getMode())) {
            if (request.getTargetAlbumIds() != null) {
                for (Long albumId : request.getTargetAlbumIds()) {
                    albumService.addContentToAlbum(albumId, savedPost.getId(), AlbumContent.ContentType.POST);
                }
            }
            // Folder assignment logic can be added similarly if needed
        }

        return savedPost;
    }

    // 3. 게시글 수정
    // 3. 게시글 수정
    @Transactional
    public Post updatePost(User user, Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        post.update(
                request.getTitle(),
                request.getBlocks(),
                request.getStickers(),
                request.getFloatingTexts(),
                request.getFloatingImages(),
                request.getIsFavorite());

        hashtagService.processHashtags(post, request.getHashtags());

        return post;
    }

    // 4. 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}