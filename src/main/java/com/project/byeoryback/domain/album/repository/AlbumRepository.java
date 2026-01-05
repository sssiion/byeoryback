package com.project.byeoryback.domain.album.repository;

import com.project.byeoryback.domain.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByUserId(Long userId);

    List<Album> findByParentId(Long parentId);

    Long countByParentId(Long parentId);
}
