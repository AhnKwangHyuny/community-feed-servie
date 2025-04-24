package org.faddy.community_feed.post.application.dto;

import java.util.List;
import org.faddy.community_feed.post.domain.PostPublicationState;

public record CreatePostRequestDto(
    String content,
    PostPublicationState state,
    List<Long> imageIds
) {
    // 기존 생성자 호환성을 위한 생성자
    public CreatePostRequestDto(String content, PostPublicationState state) {
        this(content, state, List.of());
    }

    @Override
    public String toString() {
        return "CreatePostRequestDto{" +
            "content='" + content + '\'' +
            ", state=" + state +
            ", imageIds=" + imageIds +
            '}';
    }
}