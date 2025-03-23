package org.faddy.post.domain;

import org.faddy.User.domain.User;
import org.faddy.common.domain.PositiveInteger;
import org.faddy.common.trait.Likeable;
import org.faddy.post.domain.content.PostContent;
import org.faddy.post.domain.content.PostPublicationState;

public class Post implements Likeable {

    private Long id;
    private final User writer;
    private final PostContent content;
    private final PositiveInteger likeCount;
    private PostPublicationState state;

    public Post(Long id, User writer, String content) {
        this.content = new PostContent(content);
        if (writer == null) {
            throw new IllegalArgumentException("게시자가 존재하지 않습니다.");
        }

        this.writer = writer;
        this.id = id;
        this.likeCount = new PositiveInteger();
        this.state = PostPublicationState.PUBLIC;
    }

    @Override
    public void like(User user) {
        this.validateLikeOperation(user); // 사용자 검증

        this.likeCount.increase();
    }

    @Override
    public void unlike(User user) {
        this.validateLikeOperation(user); // 사용자 검증

        this.likeCount.decrease();

    }

    @Override
    public int getLikeCount() {
        return this.likeCount.getCount();
    }

    @Override
    public void validateLikeOperation(User user) {
        if(writer.equals(user)) {
            throw new IllegalArgumentException("작성자는 좋아요 또는 싫어요를 누를 수 없습니다.");
        }
    }

    public void updatePost(User user , String content , PostPublicationState state) {
        if (!writer.equals(user)) {
            throw new IllegalArgumentException("작성자가 아니면 게시글을 수정할 수 없습니다.");
        }

        this.content.updateContent(content);
        this.state = state;
    }

}
