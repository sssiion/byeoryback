package com.project.byeoryback.domain.album.entity;

import com.project.byeoryback.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "album_contents", indexes = {
        @Index(name = "idx_album_content_parent", columnList = "parent_album_id, parent_folder_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AlbumContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_album_id")
    private Album parentAlbum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_post_id")
    private Post childPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_folder_id")
    private Folder childFolder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    public enum ContentType {
        POST, FOLDER
    }
}
