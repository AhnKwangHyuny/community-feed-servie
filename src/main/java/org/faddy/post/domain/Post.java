package org.faddy.post.domain;

import org.faddy.User.domain.User;
import org.faddy.common.domain.PositiveInteger;
import org.faddy.common.domain.trait.Likeable;
import org.faddy.post.domain.content.PostContent;

public class Post implements Likeable {

    private Long id;
    private final User writer;
    private final PostContent content;
    private final PositiveInteger likeCount;

    public Post(Long id, User writer, String content) {
        this.content = new PostContent(content);
        if (writer == null) {
            throw new IllegalArgumentException("게시자가 존재하지 않습니다.");
        }

        this.writer = writer;
        this.id = id;
        this.likeCount = new PositiveInteger();
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
        return this.likeCount.getLikeCount();
    }

    @Override
    public void validateLikeOperation(User user) {
        if(writer.equals(user)) {
            throw new IllegalArgumentException("작성자는 좋아요 또는 싫어요를 누를 수 없습니다.");
        }
    }


}
