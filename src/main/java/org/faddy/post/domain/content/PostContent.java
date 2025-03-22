package org.faddy.post.domain.content;

public class PostContent extends Content {

    private static final int MAX_POST_LENGTH = 500;
    private static final int MIN_POST_LENGTH = 5;

    public PostContent(String content) {
        super(content);
    }

    @Override
    public void checkText(String contentTest) {
        if (contentTest.isEmpty() || contentTest == null) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        if (contentTest.length() < MIN_POST_LENGTH) {
            throw new IllegalArgumentException("게시글은 최소 5자 이상어야 합니다.");
        }

        if (contentTest.length() > MAX_POST_LENGTH) {
            throw new IllegalArgumentException("게시글은 최대 500글자 이상 넘길 수 없습니다.");
        }
    }
}
