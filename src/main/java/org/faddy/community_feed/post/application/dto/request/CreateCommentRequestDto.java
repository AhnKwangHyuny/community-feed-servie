package org.faddy.community_feed.post.application.dto.request;

public record CreateCommentRequestDto(
    Long postId,
    Long authorId,
    String content
) {

}
