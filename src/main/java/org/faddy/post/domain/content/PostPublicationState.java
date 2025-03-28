package org.faddy.post.domain.content;

import lombok.Getter;

@Getter
public enum PostPublicationState {
    PUBLIC("P", "전체 공개"),
    ONLY_FOLLOWER("F", "팔로워만"),
    PRIVATE("X", "비공개");

    private final String code;
    private final String description;

    PostPublicationState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PostPublicationState getStateFromCode(String code) {
        for (PostPublicationState state : PostPublicationState.values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        throw new IllegalArgumentException("알 수 없는 게시글 공개 상태 코드입니다: " + code);
    }
}