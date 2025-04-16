package org.faddy.community_feed.post.application.interfaces;

import org.faddy.community_feed.post.domain.Post;

public interface PostRepository {

    Post findById(Long id);
    Post save(Post post);
    Post publish(Post post);
}
