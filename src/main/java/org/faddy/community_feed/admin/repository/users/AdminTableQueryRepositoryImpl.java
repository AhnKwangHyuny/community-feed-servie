package org.faddy.community_feed.admin.repository.users;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableResponseDto;
import org.faddy.community_feed.admin.ui.query.AdminUserTableQuery;
import org.faddy.community_feed.auth.repository.entity.QUserAuthEntity;
import org.faddy.community_feed.user.application.interfaces.UserRepository;
import org.faddy.community_feed.user.repository.entity.QUserEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminTableQueryRepositoryImpl implements AdminUserTableQuery {

    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;
    private static final QUserEntity qUserEntity = QUserEntity.userEntity;
    private static final QUserAuthEntity qUserAuthEntity = QUserAuthEntity.userAuthEntity;

    @Override
    public GetTableListResponseDto<GetUserTableResponseDto> getUserTableData(
        GetUserTableRequestDto dto) {

        // 검색 조건 생성
        BooleanBuilder whereConditions = createWhereConditions(dto);

        // 전체 사용자 수 카운트
        int totalCount = countTotalUsers(whereConditions);

        // 사용자 데이터 조회 및 조인
        List<GetUserTableResponseDto> userTableData = queryFactory
            .select(Projections.constructor(
                GetUserTableResponseDto.class,
                qUserEntity.id, // userId
                qUserEntity.name, // name
                qUserAuthEntity.email, // email
                qUserAuthEntity.role, // role
                qUserEntity.regDt, // createdAt
                qUserEntity.updDt, // updatedAt
                qUserAuthEntity.lastLoginAt // lastLoginAt
            ))
            .from(qUserEntity)
            .join(qUserAuthEntity).on(qUserEntity.id.eq(qUserAuthEntity.userId))
            .where(whereConditions)
            .orderBy(qUserEntity.id.desc()) // 최신순 정렬
            .offset(dto.getOffset())
            .limit(dto.getLimit())
            .fetch();

        // 응답 DTO 생성 및 반환
        return GetTableListResponseDto.<GetUserTableResponseDto>builder()
            .totalCount(totalCount)
            .tableData(userTableData)
            .build();
    }

    /**
     * 검색 조건 생성
     */
    private BooleanBuilder createWhereConditions(GetUserTableRequestDto dto) {
        BooleanBuilder whereConditions = new BooleanBuilder();

        // 이름으로 검색 조건 추가
        String name = dto.getName(); // 지역 변수로 빼서 가독성 향상

        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            whereConditions.and(qUserEntity.name.containsIgnoreCase(name));
        }

        // 추후 필요시 여기에 추가 검색 조건 구현
        // 예: 이메일, 역할 등으로 검색
        return whereConditions;
    }

    /**
     * 총 사용자 수 카운트
     */
    private int countTotalUsers(BooleanBuilder whereConditions) {
        Long count = queryFactory
            .select(qUserEntity.count())
            .from(qUserEntity)
            .join(qUserAuthEntity).on(qUserEntity.id.eq(qUserAuthEntity.userId))
            .where(whereConditions)
            .fetchOne();

        return count != null ? count.intValue() : 0;
    }


}