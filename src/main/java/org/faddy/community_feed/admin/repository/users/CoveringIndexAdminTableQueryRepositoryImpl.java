package org.faddy.community_feed.admin.repository.users;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableResponseDto;
import org.faddy.community_feed.admin.ui.query.AdminUserTableQuery;
import org.faddy.community_feed.auth.repository.entity.QUserAuthEntity;
import org.faddy.community_feed.user.application.interfaces.UserRepository;
import org.faddy.community_feed.user.repository.entity.QUserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Primary // 기본 구현체로 이 클래스를 사용하도록 설정
public class CoveringIndexAdminTableQueryRepositoryImpl implements AdminUserTableQuery {

    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;
    private static final QUserEntity qUserEntity = QUserEntity.userEntity;
    private static final QUserAuthEntity qUserAuthEntity = QUserAuthEntity.userAuthEntity;

    public CoveringIndexAdminTableQueryRepositoryImpl(JPAQueryFactory queryFactory, UserRepository userRepository) {
        this.queryFactory = queryFactory;
        this.userRepository = userRepository;
    }

    @Override
    public GetTableListResponseDto<GetUserTableResponseDto> getUserTableData(
        GetUserTableRequestDto dto) {

        // 검색 조건 생성
        BooleanBuilder whereConditions = createWhereConditions(dto);

        // 전체 사용자 수 카운트
        int totalCount = countTotalUsers(whereConditions);

        List<Long> userIds = queryFactory
            .select(qUserEntity.id)
            .from(qUserEntity)
            .join(qUserAuthEntity).on(qUserEntity.id.eq(qUserAuthEntity.userId))
            .where(whereConditions)
            .orderBy(qUserEntity.id.desc()) // 최신순 정렬
            .offset(dto.getOffset())
            .limit(dto.getLimit())
            .fetch();

        // 결과가 없는 경우 빈 리스트 반환
        if (userIds.isEmpty()) {
            return GetTableListResponseDto.<GetUserTableResponseDto>builder()
                .totalCount(0)
                .tableData(Collections.emptyList())
                .build();
        }

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
            .where(qUserEntity.id.in(userIds))
            .orderBy(qUserEntity.id.desc()) // 최신순 정렬
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
        String name = dto.getName();

        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            whereConditions.and(qUserEntity.name.containsIgnoreCase(name));
        }

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