package org.faddy.community_feed.post.repository.post_queue.interfaces;

import org.faddy.community_feed.post.repository.entity.post.PostEntity;

public interface UserPostQueueCommandRepository {
    void publishPost(PostEntity postEntity);
    void saveFollowPost(Long userId, Long targetId);
    void deleteUnfollowPost(Long userId, Long targetId);
}
