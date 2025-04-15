package org.fastcampus.community_feed.post.application.interfaces;

import org.fastcampus.community_feed.post.domain.Post;

public interface PostRepository {

    Post findById(Long id);
    Post save(Post post);
    Post publish(Post post);
}
