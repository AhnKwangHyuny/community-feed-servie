package org.fastcampus.community_feed.post.repository.post_queue;

import java.util.List;
import org.fastcampus.community_feed.post.repository.entity.post.PostEntity;

public interface UserQueueRedisRepository {
    void publishPostToUserListQueue(PostEntity post, List<Long> userIdList);
    void publishPostListToUserQueue(List<PostEntity> postEntities, Long userId);
    void deletePostToUserQueue(Long userId, Long targetUserId);
}
