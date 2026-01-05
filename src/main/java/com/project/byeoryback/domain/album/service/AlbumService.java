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
import com.project.byeoryback.domain.user.entity.User;
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
    private final com.project.byeoryback.domain.hashtag.service.HashtagService hashtagService;

    public List<AlbumContentResponseDto> getAlbumContents(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        List<AlbumContentResponseDto> result = new ArrayList<>();
        Set<Long> addedPostIds = new HashSet<>();

        // 1. Get Sub-Albums (Nested)
        List<Album> subAlbums = albumRepository.findByParentId(albumId);
        List<Long> subAlbumIds = new ArrayList<>();
        for (Album subAlbum : subAlbums) {

            Long folderCount = getFolderCount(subAlbum.getId());
            Long postCount = getPostCount(subAlbum);

            result.add(AlbumContentResponseDto.fromAlbum(subAlbum, folderCount, postCount));
            subAlbumIds.add(subAlbum.getId());
        }

        // 2. Get Manual Contents (AlbumContent - Posts)
        List<AlbumContent> manualContents = albumContentRepository.findAllByParentAlbumId(albumId);
        for (AlbumContent content : manualContents) {
            if (content.getContentType() == AlbumContent.ContentType.POST) {
                Post post = content.getChildPost();
                if (post != null) {
                    result.add(AlbumContentResponseDto.fromPost(post));
                    addedPostIds.add(post.getId());
                }
            }
        }

        // Pre-fetch contents of sub-albums to exclude them from auto-grouping
        Set<Long> subAlbumPostIds = new HashSet<>();
        if (!subAlbumIds.isEmpty()) {
            List<AlbumContent> subAlbumContents = albumContentRepository.findAllByParentAlbumIdIn(subAlbumIds);
            for (AlbumContent content : subAlbumContents) {
                if (content.getContentType() == AlbumContent.ContentType.POST && content.getChildPost() != null) {
                    subAlbumPostIds.add(content.getChildPost().getId());
                }
            }
        }

        // 3. Get Auto Contents (Hashtag match)
        if (album.getRepresentativeHashtag() != null) {
            List<PostHashtag> taggedPosts = postHashtagRepository
                    .findAllByHashtagId(album.getRepresentativeHashtag().getId());
            for (PostHashtag ph : taggedPosts) {
                Post post = ph.getPost();
                if (!addedPostIds.contains(post.getId()) && !subAlbumPostIds.contains(post.getId())) {
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

    // New helper methods
    private Long getFolderCount(Long albumId) {
        return albumRepository.countByParentId(albumId);
    }

    private Long getPostCount(Album album) {
        // Collect all related album IDs (recursive)
        Set<Long> allAlbumIds = new HashSet<>();
        collectSubAlbumIds(album.getId(), allAlbumIds);
        allAlbumIds.add(album.getId());

        Set<Long> uniquePostIds = new HashSet<>();

        // 1. Manual Posts for all current and sub albums
        List<AlbumContent> contents = albumContentRepository.findAllByParentAlbumIdIn(new ArrayList<>(allAlbumIds));
        for (AlbumContent content : contents) {
            if (content.getContentType() == AlbumContent.ContentType.POST && content.getChildPost() != null) {
                uniquePostIds.add(content.getChildPost().getId());
            }
        }

        // 2. Hashtag Posts for all current and sub albums
        // Getting albums again to access hashtag info...
        List<Album> allAlbums = albumRepository.findAllById(allAlbumIds);
        for (Album a : allAlbums) {
            if (a.getRepresentativeHashtag() != null) {
                List<PostHashtag> taggedPosts = postHashtagRepository
                        .findAllByHashtagId(a.getRepresentativeHashtag().getId());
                for (PostHashtag ph : taggedPosts) {
                    uniquePostIds.add(ph.getPost().getId());
                }
            }
        }

        return (long) uniquePostIds.size();
    }

    private void collectSubAlbumIds(Long parentId, Set<Long> ids) {
        List<Album> children = albumRepository.findByParentId(parentId);
        for (Album child : children) {
            ids.add(child.getId());
            collectSubAlbumIds(child.getId(), ids);
        }
    }

    // CRUD Implementations
    public List<com.project.byeoryback.domain.album.dto.AlbumResponse> getAllAlbums(Long userId) {
        List<Album> albums = albumRepository.findAllByUserId(userId);
        return albums.stream()
                .map(album -> {
                    Long folderCount = getFolderCount(album.getId());
                    Long postCount = getPostCount(album);
                    return com.project.byeoryback.domain.album.dto.AlbumResponse.from(album, folderCount, postCount);
                })
                .toList();
    }

    public com.project.byeoryback.domain.album.dto.AlbumResponse getAlbumById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));
        Long folderCount = getFolderCount(album.getId());
        Long postCount = getPostCount(album);
        return com.project.byeoryback.domain.album.dto.AlbumResponse.from(album, folderCount, postCount);
    }

    @Transactional
    public com.project.byeoryback.domain.album.dto.AlbumResponse createAlbum(User user,
            com.project.byeoryback.domain.album.dto.AlbumRequest request) {
        Album parent = null;
        if (request.getParentId() != null) {
            parent = albumRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent album not found"));
        }

        com.project.byeoryback.domain.hashtag.entity.Hashtag hashtag = null;
        if (request.getTag() != null) {
            hashtag = hashtagService.findOrCreate(request.getTag());
        }

        Album album = Album.builder()
                .name(request.getName())
                .user(user)
                .parent(parent)
                .representativeHashtag(hashtag)
                .isFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false)
                .build();

        Album savedAlbum = albumRepository.save(album);
        // New album has 0 folders, 0 posts usually, but hashtag might auto-link posts
        // instantly?
        // Yes, if hashtag exists.
        Long postCount = getPostCount(savedAlbum); // Calculates based on hashtag
        return com.project.byeoryback.domain.album.dto.AlbumResponse.from(savedAlbum, 0L, postCount);
    }

    @Transactional
    public com.project.byeoryback.domain.album.dto.AlbumResponse updateAlbum(Long id,
            com.project.byeoryback.domain.album.dto.AlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        Album parent = null;
        if (request.getParentId() != null) {
            parent = albumRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent album not found"));
        }

        com.project.byeoryback.domain.hashtag.entity.Hashtag hashtag = null;
        if (request.getTag() != null) {
            hashtag = hashtagService.findOrCreate(request.getTag());
        }

        album.update(request.getName(), parent, hashtag, request.getIsFavorite());

        // Recalculate counts
        Long folderCount = getFolderCount(album.getId());
        Long postCount = getPostCount(album);

        return com.project.byeoryback.domain.album.dto.AlbumResponse.from(album, folderCount, postCount);
    }

    @Transactional
    public void deleteAlbum(Long id) {
        // 1. Find all sub-albums
        List<Album> subAlbums = albumRepository.findByParentId(id);

        // 2. Recursively delete sub-albums
        for (Album subAlbum : subAlbums) {
            deleteAlbum(subAlbum.getId());
        }

        // 3. Delete contents (AlbumContent) associated with this album

        albumRepository.deleteById(id);
    }

    @Transactional
    public void addContentToAlbum(Long albumId, Long contentId, AlbumContent.ContentType type) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        if (type == AlbumContent.ContentType.POST) {
            Post post = postRepository.findById(contentId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

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

    @Transactional
    public void moveContent(Long targetAlbumId, Long sourceAlbumId, Long contentId, AlbumContent.ContentType type) {
        addContentToAlbum(targetAlbumId, contentId, type);
        removeContentFromAlbum(sourceAlbumId, contentId, type);
    }
}
