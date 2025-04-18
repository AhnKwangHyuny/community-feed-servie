package org.faddy.community_feed.admin.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.admin.ui.dto.users.GetDailyRegisteredUserResponseDto;
import org.faddy.community_feed.admin.ui.query.UserStatsQueryRepository;
import org.faddy.community_feed.common.utils.TimeCalculator;
import org.faddy.community_feed.user.repository.entity.QUserEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatsQueryRepositoryImpl implements UserStatsQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QUserEntity qUserEntity = QUserEntity.userEntity;


    @Override
    public List<GetDailyRegisteredUserResponseDto> getDailyRegisteredUserStats(int beforeDays) {
        return queryFactory
            .select(
                Projections.fields(
                    GetDailyRegisteredUserResponseDto.class,
                    qUserEntity.regDate.as("date"),
                    qUserEntity.count().as("count")
                )
            ).from(qUserEntity)
            .where(qUserEntity.regDate.after(TimeCalculator.getDateDaysAgo(beforeDays)))
            .groupBy(qUserEntity.regDate)
            .orderBy(qUserEntity.regDate.asc())
            .fetch();
    }
}
