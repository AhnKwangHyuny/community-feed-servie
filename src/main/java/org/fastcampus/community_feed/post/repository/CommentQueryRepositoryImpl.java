package org.fastcampus.community_feed.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.post.application.dto.GetContentResponseDto;
import org.fastcampus.community_feed.post.repository.entity.comment.QCommentEntity;
import org.fastcampus.community_feed.post.repository.entity.like.LikeTarget;
import org.fastcampus.community_feed.post.repository.entity.like.QLikeEntity;
import org.fastcampus.community_feed.user.repository.entity.QUserEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    private static final QCommentEntity commentEntity = QCommentEntity.commentEntity;
    private static final QUserEntity userEntity = QUserEntity.userEntity;
    private static final QLikeEntity likeEntity = QLikeEntity.likeEntity;

    public List<GetContentResponseDto> getCommentList(Long postId, Long userId, Long lastContentId) {
        return queryFactory
                .select(
                        Projections.fields(
                                GetContentResponseDto.class,
                                commentEntity.id.as("id"),
                                commentEntity.content.as("content"),
                                userEntity.id.as("userId"),
                                userEntity.name.as("userName"),
                                userEntity.profileImage.as("userProfileImage"),
                                commentEntity.likeCounter.as("likeCount"),
                                commentEntity.regDt.as("createdAt"),
                                commentEntity.updDt.as("updatedAt"),
                                likeEntity.isNotNull().as("isLikedByMe")
                        )
                )
                .from(commentEntity)
                .join(userEntity).on(commentEntity.author.id.eq(userEntity.id))
                .leftJoin(likeEntity).on(hasLike(userId))
                .where(
                        commentEntity.post.id.eq(postId),
                        hasLastData(lastContentId)
                )
                .orderBy(commentEntity.id.desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression hasLastData(Long lastId) {
        if (lastId == null) {
            return null;
        }

        return commentEntity.id.lt(lastId);
    }

    private BooleanExpression hasLike(Long userId) {
        if (userId == null) {
            return null;
        }

        return commentEntity.id
                .eq(likeEntity.id.targetId)
                .and(likeEntity.id.targetType.eq(LikeTarget.COMMENT.name()))
                .and(likeEntity.id.userId.eq(userId));
    }

}
