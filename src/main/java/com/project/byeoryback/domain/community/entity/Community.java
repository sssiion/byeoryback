package com.project.byeoryback.domain.community.entity;

import com.project.byeoryback.domain.message.entity.Message;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Post와 연결 (외래키). 보통 게시글 하나가 커뮤니티 글 하나가 되므로 OneToOne이 적합합니다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 작성자 (User) - Post에도 있지만, 쿼리 성능(조인 최소화)을 위해 여기서도 바로 접근 가능하게 두는 경우가 많습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 댓글 리스트 (Message와의 양방향 매핑)
    // orphanRemoval = true: Community 글이 삭제되면 댓글도 다 같이 삭제됨
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    // 좋아요 수 (단순 카운트 저장용)
    // 실제 누가 좋아요 눌렀는지 체크하려면 별도의 'CommunityLike' 테이블이 필요하지만,
    // 여기서는 화면 표시용 숫자를 저장합니다.
    @Column(nullable = false)
    @ColumnDefault("0")
    @Builder.Default
    private Long likeCount = 0L;

    // 조회수 (필요할 것 같아서 추가했습니다)
    @Column(nullable = false)
    @ColumnDefault("0")
    @Builder.Default
    private Long viewCount = 0L;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 좋아요 수 증가 메서드 예시
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수 감소 메서드 예시
    public void decreaseLikeCount() {
        if (this.likeCount > 0) this.likeCount--;
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount++;
    }
}