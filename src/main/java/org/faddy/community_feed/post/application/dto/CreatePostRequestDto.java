package org.faddy.community_feed.post.application.dto;

import org.faddy.community_feed.post.domain.PostPublicationState;

public record CreatePostRequestDto( String content, PostPublicationState state) {

}
