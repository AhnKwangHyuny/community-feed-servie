package org.faddy.comment.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.User.application.repo.entity.UserEntity;
import org.faddy.comment.domain.Comment;
import org.faddy.common.infrastructure.entity.TimeBaseEntity;
import org.faddy.post.application.repo.entity.PostEntity;

@Entity
@Table(name = "community_comments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    /**
     * Comment 도메인 객체로부터 CommentEntity 생성
     */
    public static CommentEntity fromComment(Comment comment) {
        UserEntity userEntity = UserEntity.fromUser(comment.getAuthor());
        PostEntity postEntity = PostEntity.fromPost(comment.getPost());

        return CommentEntity.builder()
            .id(comment.getId())
            .author(userEntity)
            .post(postEntity)
            .content(comment.getContent().getContentText())
            .likeCount(comment.getLikeCount())
            .build();
    }

    /**
     * CommentEntity로부터 Comment 도메인 객체를 생성
     */
    public Comment toComment() {
        return Comment.builder()
            .id(this.id)
            .author(this.author.toUser())
            .post(this.post.toPost())
            .content(this.content)
            .likeCount(this.likeCount)
            .build();
    }
}