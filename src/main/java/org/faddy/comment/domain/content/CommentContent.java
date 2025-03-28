package org.faddy.comment.domain.content;


import org.faddy.common.domain.content.Content;

public class CommentContent extends Content {

    private static final int MAX_REPLY_LENGTH = 100;
    private static final int MIN_REPLY_LENGTH = 5;

    public CommentContent(String content) {
        super(content);
    }

    @Override
    public void checkText(String contentTest) {
        if (contentTest.isEmpty() || contentTest == null) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        if (contentTest.length() < MIN_REPLY_LENGTH) {
            throw new IllegalArgumentException("댓글은 최소 5자 이상어야 합니다.");
        }

        if (contentTest.length() > MAX_REPLY_LENGTH) {
            throw new IllegalArgumentException("댓글은 최대 500글자 이상 넘길 수 없습니다.");
        }
    }

}
