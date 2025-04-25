package org.faddy.community_feed.post.application.service;

import org.faddy.community_feed.post.application.dto.request.CreatePostRequestDto;
import org.faddy.community_feed.post.application.dto.request.UpdatePostRequestDto;
import org.faddy.community_feed.post.domain.Post;

public interface PostService {
    Post createPost(Long userId , CreatePostRequestDto dto);
    Post updatePost(Long postId, Long userId, UpdatePostRequestDto dto);
    void likePost(Long userId, Long targetId);
    void unlikePost(Long userId, Long targetId);
    Post getPost(Long postId);


}