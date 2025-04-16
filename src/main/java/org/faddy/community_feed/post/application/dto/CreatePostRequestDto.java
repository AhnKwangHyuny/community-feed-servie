package org.faddy.community_feed.post.application.dto;

import org.faddy.community_feed.post.domain.PostPublicationState;

public record CreatePostRequestDto(Long userId, String content, PostPublicationState state) {
}
