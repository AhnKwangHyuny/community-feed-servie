package org.faddy.User.infrastructure.repo.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.User.application.dto.GetUserListResponseDto;
import org.faddy.User.infrastructure.repo.entity.QUserEntity;
import org.faddy.User.infrastructure.repo.entity.QUserRelationEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserListPagingQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QUserEntity userEntity = QUserEntity.userEntity;
    private static final QUserRelationEntity relationEntity = QUserRelationEntity.userRelationEntity;

    public List<GetUserListResponseDto> getFollowerList(Long userId, Long lastFollowerId) {
        return queryFactory
            .select(
                Projections.fields(
                    GetUserListResponseDto.class,
                    userEntity.name.as("name"),
                    userEntity.profileImageUrl.as("profileImage")
                )
            )
            .from(relationEntity)
            .join(userEntity).on(relationEntity.followingId.eq(userEntity.id))
            .where(
                relationEntity.followerId.eq(userId),
                hasLastData(lastFollowerId)
            )
            .orderBy(userEntity.id.desc())
            .limit(100L)
            .fetch();
    }

    private BooleanExpression hasLastData(Long lastId) {
        if (lastId == null) {
            return null;
        }

        return userEntity.id.lt(lastId);
    }

}