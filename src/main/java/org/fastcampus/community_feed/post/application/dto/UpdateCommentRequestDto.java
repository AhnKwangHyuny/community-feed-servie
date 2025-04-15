package org.fastcampus.community_feed.post.application.dto;

public record UpdateCommentRequestDto(
    Long authorId,
    String content
) {

}
