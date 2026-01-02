package com.project.byeoryback.domain.album.repository;

import com.project.byeoryback.domain.album.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUserId(Long userId);

    List<Folder> findAllByParentFolderId(Long parentFolderId);
}
