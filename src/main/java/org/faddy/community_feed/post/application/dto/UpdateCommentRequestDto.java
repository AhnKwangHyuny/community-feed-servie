package org.faddy.community_feed.post.application.dto;

public record UpdateCommentRequestDto(
    Long authorId,
    String content
) {

}
