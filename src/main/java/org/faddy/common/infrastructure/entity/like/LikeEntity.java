package org.faddy.common.infrastructure.entity.like;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.User.domain.User;
import org.faddy.comment.domain.Comment;
import org.faddy.common.infrastructure.entity.TimeBaseEntity;
import org.faddy.post.domain.Post;

@Entity
@Table(name="community_likes")
@NoArgsConstructor
@Getter
public class LikeEntity extends TimeBaseEntity {

    @EmbeddedId
    private LikeId id;

    public LikeEntity(Post post, User likeedUser) {
        this.id = new LikeId(post.getId(), likeedUser.getId(), LikeTarget.POST.name());
    }

    public LikeEntity(Comment comment, User likeedUser) {
        this.id = new LikeId(comment.getId(), likeedUser.getId(), LikeTarget.COMMENT.name());
    }
}