package org.faddy.community_feed.post.repository.post_queue;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.post.repository.entity.post.PostEntity;
import org.faddy.community_feed.post.repository.jpa.JpaPostRepository;
import org.faddy.community_feed.post.repository.post_queue.interfaces.UserPostQueueCommandRepository;
import org.faddy.community_feed.post.repository.post_queue.interfaces.UserQueueRedisRepository;
import org.faddy.community_feed.user.repository.entity.UserEntity;
import org.faddy.community_feed.user.repository.jpa.JpaUserRelationRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPostQueueCommandRepositoryImpl implements UserPostQueueCommandRepository {
    private final JpaPostRepository jpaPostRepository;
    private final JpaUserRelationRepository jpaUserRelationRepository;
    private final UserQueueRedisRepository queueRepository;

    public void publishPost(PostEntity postEntity) {
        UserEntity authorEntity = postEntity.getAuthor();
        List<Long> followers = jpaUserRelationRepository.findFollowers(authorEntity.getId());
        queueRepository.publishPostToUserListQueue(postEntity, followers);
    }

    public void saveFollowPost(Long userId, Long targetId) {
        List<PostEntity> postEntities = jpaPostRepository.findAllPostIdsByAuthorId(targetId);
        queueRepository.publishPostListToUserQueue(postEntities, userId);
    }

    public void deleteUnfollowPost(Long userId, Long targetId) {
        queueRepository.deletePostToUserQueue(userId, targetId);
    }
}
