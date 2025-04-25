package org.faddy.community_feed.post.application.dto.request;

public record UpdateCommentRequestDto(
    Long authorId,
    String content
) {

}
