package com.project.byeoryback.domain.album.repository;

import com.project.byeoryback.domain.album.entity.AlbumContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlbumContentRepository extends JpaRepository<AlbumContent, Long> {
    List<AlbumContent> findAllByParentAlbumId(Long parentAlbumId);

    List<AlbumContent> findAllByParentFolderId(Long parentFolderId);

    void deleteAllByChildPostId(Long postId);
}
