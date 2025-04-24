package org.faddy.community_feed.post.application.dto;

import java.util.List;
import org.faddy.community_feed.post.domain.PostPublicationState;

public record UpdatePostRequestDto(
    String content,
    PostPublicationState state,
    List<Long> imageIds
) {
    // 기존 생성자 호환성을 위한 생성자
    public UpdatePostRequestDto(String content, PostPublicationState state) {
        this(content, state, null);
    }
}