package org.faddy.community_feed.post.application.interfaces;

import org.faddy.community_feed.post.application.dto.CreatePostRequestDto;
import org.faddy.community_feed.post.application.dto.UpdatePostRequestDto;
import org.faddy.community_feed.post.domain.Post;

public interface PostService {
    Post createPost(Long userId , CreatePostRequestDto dto);
    Post updatePost(Long postId, Long userId, UpdatePostRequestDto dto);
    void likePost(Long userId, Long targetId);
    void unlikePost(Long userId, Long targetId);
    Post getPost(Long postId);


}