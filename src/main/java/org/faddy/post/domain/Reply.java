package org.faddy.post.domain;

import java.io.Writer;
import org.faddy.User.domain.User;
import org.faddy.common.domain.PositiveInteger;
import org.faddy.common.domain.trait.Likeable;
import org.faddy.post.domain.content.ReplyContent;

public class Reply implements Likeable {

    private final Long id;
    private final User writer;
    private final Post post;
    private final ReplyContent content;
    private final PositiveInteger likeCount;


    public Reply(Long id, User writer, Post post, String content) {

        if (writer == null) {
            throw new IllegalArgumentException("댓글 사용자가 존재하지 않습니다.");
        }

        if (post == null) {
            throw new IllegalArgumentException("댓글 달 피드가 존재하지 않습니다.");
        }

        this.id = id;
        this.writer = writer;
        this.post = post;
        this.content = new ReplyContent(content);
        this.likeCount = new PositiveInteger();
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
        return this.likeCount.getLikeCount();
    }

    @Override
    public void validateLikeOperation(User user) {
        if(!this.writer.equals(user)) {
            throw new IllegalArgumentException("작성자는 좋아요를 누를 수 없습니다.");
        }
    }
}
