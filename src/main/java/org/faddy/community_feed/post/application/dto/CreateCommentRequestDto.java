package org.faddy.community_feed.post.application.dto;

public record CreateCommentRequestDto(
    Long postId,
    Long authorId,
    String content
) {

}
