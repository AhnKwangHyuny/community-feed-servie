package org.faddy.community_feed.post.application.dto.request;

import java.util.List;
import org.faddy.community_feed.post.domain.enumeration.PostPublicationState;

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