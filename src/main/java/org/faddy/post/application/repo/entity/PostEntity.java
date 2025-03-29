package org.faddy.post.application.repo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.User.infrastructure.repo.entity.UserEntity;
import org.faddy.common.infrastructure.entity.TimeBaseEntity;
import org.faddy.post.application.common.converter.PostPublicationStateConverter;
import org.faddy.post.domain.Post;
import org.faddy.post.domain.content.PostPublicationState;

@Entity
@Table(name = "community_posts")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Convert(converter = PostPublicationStateConverter.class)
    @Column(name = "publication_state", nullable = false, length = 1)
    private PostPublicationState state;

    /**
     * Post 도메인 객체로부터 PostEntity 생성
     */
    public static PostEntity fromPost(Post post) {

        UserEntity userEntity = UserEntity.fromUser(post.getAuthor());

        return PostEntity.builder()
            .id(post.getId())
            .author(userEntity)
            .content(post.getContentText())
            .likeCount(post.getLikeCount())
            .state(post.getState())
            .build();
    }

    /**
     * PostEntity로부터 Post 도메인 객체를 생성
     */
    public Post toPost() {
        return Post.builder()
            .id(this.id)
            .writer(this.author.toUser()) // UserEntity에 toUser 메서드 가정
            .content(this.content)
            .likeCount(this.likeCount)
            .state(this.state)
            .build();
    }

}