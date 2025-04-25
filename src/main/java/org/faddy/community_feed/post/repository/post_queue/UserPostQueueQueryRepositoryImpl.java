package org.faddy.community_feed.post.repository.post_queue;

import static org.faddy.community_feed.post.repository.entity.like.QLikeEntity.likeEntity;
import static org.faddy.community_feed.post.repository.entity.post.QPostEntity.postEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.post.application.dto.response.GetPostContentResponseDto;
import org.faddy.community_feed.post.repository.entity.post.PostEntity;
import org.faddy.community_feed.post.repository.post_queue.interfaces.UserPostQueueQueryRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPostQueueQueryRepositoryImpl implements UserPostQueueQueryRepository {

    private final UserQueueRedisRepositoryImpl queueRepository;

    @Override
    public List<GetPostContentResponseDto> getContentResponse(Long userId, Long lastContentId) {
        List<PostEntity> postEntities = queueRepository.getPostsByUserId(userId);

        List<GetPostContentResponseDto> result = new ArrayList<>();

        for (PostEntity postEntity : postEntities) {
            GetPostContentResponseDto content = GetPostContentResponseDto.builder()
                    .id(postEntity.getId())
                    .content(postEntity.getContent())
                    .commentCount(postEntity.getCommentCounter())
                    .build();
            result.add(content);
        }
        return result;
    }

    private BooleanExpression hasLastData(Long lastId) {
        if (lastId == null) {
            return null;
        }

        return postEntity.id.lt(lastId);
    }

    private BooleanExpression hasLike(Long userId) {
        if (userId == null) {
            return null;
        }

        return postEntity.id
                .eq(likeEntity.id.targetId)
                .and(likeEntity.id.targetType.eq("POST"))
                .and(likeEntity.id.userId.eq(userId));
    }
}
