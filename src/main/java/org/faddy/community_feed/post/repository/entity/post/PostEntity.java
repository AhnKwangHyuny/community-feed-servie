package org.faddy.community_feed.post.repository.entity.post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.common.domain.PositiveIntegerCounter;
import org.faddy.community_feed.common.repository.TimeBaseEntity;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.enumeration.PostPublicationState;
import org.faddy.community_feed.post.domain.content.PostContent;
import org.faddy.community_feed.post.repository.entity.image.PostThumbnailEntity;
import org.faddy.community_feed.user.repository.entity.UserEntity;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "community_post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity author;

    private String content;

    @Convert(converter = PostPublicationStateConverter.class)
    private PostPublicationState state;
    private Integer likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostThumbnailEntity> thumbnails = new ArrayList<>();

    @ColumnDefault("0")
    private int commentCounter;

    @ColumnDefault("0")
    private int viewCounter;

    public PostEntity(Post post) {
        this.id = post.getId();
        this.author = new UserEntity(post.getAuthor());
        this.content = post.getContentText();
        this.state = post.getState();
        this.likeCount = post.getLikeCount();
    }

    public Post toPost() {
        return Post.builder()
            .id(id)
            .author(author.toUser())
            .content(new PostContent(content))
            .state(state)
            .positiveIntegerCounter(new PositiveIntegerCounter(likeCount))
            .createdAt(this.getRegDt())
            .build();
    }
}
