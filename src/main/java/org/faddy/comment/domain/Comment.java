package org.faddy.comment.domain;

import lombok.Builder;
import lombok.Getter;
import org.faddy.User.domain.User;
import org.faddy.comment.domain.content.CommentContent;
import org.faddy.common.domain.PositiveInteger;
import org.faddy.common.trait.Likeable;
import org.faddy.post.domain.Post;

@Getter
public class Comment implements Likeable {

    private final Long id;
    private final User author;
    private final Post post;
    private final CommentContent content;
    private final PositiveInteger likeCount;


    public Comment(Long id, User author, Post post, String content , Integer likeCount) {

        if (author == null) {
            throw new IllegalArgumentException("댓글 사용자가 존재하지 않습니다.");
        }

        if (post == null) {
            throw new IllegalArgumentException("댓글 달 피드가 존재하지 않습니다.");
        }

        this.id = id;
        this.author = author;
        this.post = post;
        this.content = new CommentContent(content);
        this.likeCount = likeCount != null ? new PositiveInteger(likeCount) : new PositiveInteger();
    }

    // builder용 생성자
    @Builder(builderMethodName = "builder")
    public static Comment createComment(Long id, User author, Post post, String content, Integer likeCount) {
        return new Comment(id, author, post, content, likeCount);
    }


    // like , unlike business logic
    public void like(User user) {
        this.validateLikeOperation(user);

        this.likeCount.increase();
    }

    public void unlike(User user) {
        this.validateLikeOperation(user);

        this.likeCount.decrease();
    }

    @Override
    public int getLikeCount() {
        return this.likeCount.getCount();
    }

    @Override
    public void validateLikeOperation(User user) {
        if(!this.author.equals(user)) {
            throw new IllegalArgumentException("작성자는 좋아요를 누를 수 없습니다.");
        }
    }

    public void updateReply(User user , String content) {
        if(!author.equals(user)) {
            throw new IllegalArgumentException("수정하는 사람은 본인의 댓글만 수정 가능합니다.");
        }

        this.content.updateContent(content);
    }
}
