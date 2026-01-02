package com.project.byeoryback.domain.album.service;

import com.project.byeoryback.domain.album.dto.AlbumContentResponseDto;
import com.project.byeoryback.domain.album.entity.Album;
import com.project.byeoryback.domain.album.entity.AlbumContent;
import com.project.byeoryback.domain.album.repository.AlbumContentRepository;
import com.project.byeoryback.domain.album.repository.AlbumRepository;
import com.project.byeoryback.domain.hashtag.entity.PostHashtag;
import com.project.byeoryback.domain.hashtag.repository.PostHashtagRepository;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumContentRepository albumContentRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PostRepository postRepository;

    public List<AlbumContentResponseDto> getAlbumContents(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        List<AlbumContentResponseDto> result = new ArrayList<>();
        Set<Long> addedPostIds = new HashSet<>();

        // 1. Get Manual Contents (AlbumContent)
        List<AlbumContent> manualContents = albumContentRepository.findAllByParentAlbumId(albumId);
        for (AlbumContent content : manualContents) {
            if (content.getContentType() == AlbumContent.ContentType.POST) {
                Post post = content.getChildPost();
                if (post != null) {
                    result.add(AlbumContentResponseDto.fromPost(post));
                    addedPostIds.add(post.getId());
                }
            } else if (content.getContentType() == AlbumContent.ContentType.FOLDER) {
                // Folder logic if needed
                result.add(AlbumContentResponseDto.fromFolder(content.getChildFolder()));
            }
        }

        // 2. Get Auto Contents (Hashtag match)
        if (album.getRepresentativeHashtag() != null) {
            List<PostHashtag> taggedPosts = postHashtagRepository
                    .findAllByHashtagId(album.getRepresentativeHashtag().getId());
            for (PostHashtag ph : taggedPosts) {
                Post post = ph.getPost();
                // Deduplication: Only add if not already added manually
                if (!addedPostIds.contains(post.getId())) {
                    result.add(AlbumContentResponseDto.fromPost(post));
                    addedPostIds.add(post.getId());
                }
            }
        }

        return result;
    }

    public List<Post> getUnclassifiedPosts(Long userId) {
        return postRepository.findUnclassifiedPosts(userId);
    }

    @Transactional
    public void addContentToAlbum(Long albumId, Long contentId, AlbumContent.ContentType type) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        if (type == AlbumContent.ContentType.POST) {
            Post post = postRepository.findById(contentId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

            // Check if already exists
            boolean exists = albumContentRepository.findAllByParentAlbumId(albumId).stream()
                    .anyMatch(ac -> ac.getChildPost() != null && ac.getChildPost().getId().equals(contentId));

            if (!exists) {
                albumContentRepository.save(AlbumContent.builder()
                        .parentAlbum(album)
                        .childPost(post)
                        .contentType(type)
                        .build());
            }
        }
        // Add folder logic if needed
    }

    @Transactional
    public void removeContentFromAlbum(Long albumId, Long contentId, AlbumContent.ContentType type) {
        List<AlbumContent> contents = albumContentRepository.findAllByParentAlbumId(albumId);
        for (AlbumContent content : contents) {
            if (type == AlbumContent.ContentType.POST && content.getChildPost() != null
                    && content.getChildPost().getId().equals(contentId)) {
                albumContentRepository.delete(content);
            }
        }
    }
}
