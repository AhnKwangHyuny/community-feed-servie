package org.faddy.community_feed.post.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.faddy.community_feed.post.application.dto.request.CreatePostRequestDto;
import org.faddy.community_feed.post.application.dto.request.LikeRequestDto;
import org.faddy.community_feed.post.application.dto.request.UpdatePostRequestDto;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.enumeration.PostPublicationState;
import org.faddy.community_feed.post.domain.content.Content;
import org.junit.jupiter.api.Test;

class PostServiceTest extends PostServiceTestTemplate {
    CreatePostRequestDto dto = new CreatePostRequestDto("test-content", PostPublicationState.PUBLIC);
    Long testUserId = 123213L;

    @Test
    void givenPostRequestDtoWhenCreateThenReturnPost() {
        // when
        Post savedPost = postService.createPost(testUserId, dto);

        // then
        Post post = postService.getPost(savedPost.getId());
        assertEquals(savedPost, post);
    }

    @Test
    void givenCreatePostWhenUpdateThenReturnUpdatedPost() {
        // given
        Post savedPost = postService.createPost(testUserId, dto);

        // when
        UpdatePostRequestDto updateDto = new UpdatePostRequestDto("updated-content", PostPublicationState.PRIVATE);
        Post updatedPost = postService.updatePost(savedPost.getId(),testUserId ,  updateDto);

        // then
        Content content = updatedPost.getContent();
        assertEquals("updated-content", content.getContentText());
        assertEquals(PostPublicationState.PRIVATE, updatedPost.getState());
    }

    @Test
    void givenCreatedPostWhenLikedThenReturnPostWithLike() {
        // given
        Post savedPost = postService.createPost(testUserId, dto);

        // when
        LikeRequestDto likeRequestDto = new LikeRequestDto(otherUser.getId(), savedPost.getId());
        postService.likePost(testUserId, likeRequestDto.targetId());

        // then
        assertEquals(1, savedPost.getLikeCount());
    }

    @Test
    void givenCreatedPostWhenLikedTwiceThenReturnPostWithLike() {
        // given
        Post savedPost = postService.createPost(testUserId , dto);

        // when
        LikeRequestDto likeRequestDto = new LikeRequestDto(otherUser.getId(), savedPost.getId());
        postService.likePost(testUserId , likeRequestDto.targetId());
        postService.likePost(testUserId ,likeRequestDto.targetId());

        // then
        assertEquals(1, savedPost.getLikeCount());
    }

    @Test
    void givenCreatedPostWhenUnlikedThenReturnPostWithoutLike() {
        // given
        Post savedPost = postService.createPost(testUserId, dto);

        // when
        LikeRequestDto likeRequestDto = new LikeRequestDto(otherUser.getId(), savedPost.getId());
        postService.likePost(testUserId, likeRequestDto.targetId());
        postService.unlikePost(testUserId, likeRequestDto.targetId());

        // then
        assertEquals(0, savedPost.getLikeCount());
    }

    @Test
    void givenCreatedPostWhenUnlikedTwiceThenReturnPostWithoutLike() {
        // given
        Post savedPost = postService.createPost(testUserId , dto);

        // when
        LikeRequestDto likeRequestDto = new LikeRequestDto(otherUser.getId(), savedPost.getId());
        postService.likePost(testUserId, likeRequestDto.targetId());
        postService.unlikePost(testUserId , likeRequestDto.targetId());
        postService.unlikePost(testUserId, likeRequestDto.targetId());

        // then
        assertEquals(0, savedPost.getLikeCount());
    }
}
