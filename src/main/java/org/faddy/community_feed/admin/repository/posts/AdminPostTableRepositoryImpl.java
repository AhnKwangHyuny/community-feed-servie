package org.faddy.community_feed.admin.repository.posts;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.posts.GetPostTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.posts.GetPostTableResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableResponseDto;
import org.faddy.community_feed.admin.ui.query.AdminPostTableQuery;
import org.faddy.community_feed.post.application.interfaces.PostRepository;
import org.faddy.community_feed.post.repository.entity.post.QPostEntity;
import org.faddy.community_feed.user.repository.entity.QUserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AdminPostTableRepositoryImpl implements AdminPostTableQuery {

    private final JPAQueryFactory queryFactory;
    private final PostRepository postRepository;
    private final QPostEntity qPostEntity = QPostEntity.postEntity;
    private final QUserEntity qUserEntity = QUserEntity.userEntity;

    @Override
    public GetTableListResponseDto<GetPostTableResponseDto> getPostTableData(
        GetPostTableRequestDto dto) {

        // 검색 조건 생성
        BooleanBuilder whereConditions = createWhereConditions(dto);

        // 총 게시물 수 조회
        int totalCount = countTotalPosts(whereConditions);

        // 페이지네이션 정보 계산
        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();

        List<Long> postIds = queryFactory
            .select(qPostEntity.id)
            .from(qPostEntity)
            .join(qUserEntity).on(qPostEntity.author.id.eq(qUserEntity.id))
            .where(whereConditions)
            .orderBy(qPostEntity.regDt.desc()) // 최신 게시물 순으로 정렬
            .offset(pageIndex * pageSize)
            .limit(pageSize)
            .fetch();
        System.out.println("postIds = " + postIds);
        // 조회할 데이터가 없는 경우 빈 결과 반환
        // 결과가 없는 경우 빈 리스트 반환
        if (postIds.isEmpty()) {
            return GetTableListResponseDto.<GetPostTableResponseDto>builder()
                .totalCount(0)
                .tableData(Collections.emptyList())
                .build();
        }

        // 2. ID 목록을 이용해 실제 필요한 데이터만 조회
        List<GetPostTableResponseDto> results = queryFactory
            .select(Projections.constructor(GetPostTableResponseDto.class,
                qPostEntity.id,
                qUserEntity.id,
                qUserEntity.name,
                qPostEntity.content,
                qPostEntity.regDt,
                qPostEntity.updDt))
            .from(qPostEntity)
            .join(qUserEntity).on(qPostEntity.author.id.eq(qUserEntity.id))
            .where(qPostEntity.id.in(postIds))
            .orderBy(qPostEntity.regDt.desc())
            .fetch();

        return GetTableListResponseDto.<GetPostTableResponseDto>builder()
            .totalCount(totalCount)
            .tableData(results)
            .build();
    }

    /**
     * 검색 조건 생성
     */
    private BooleanBuilder createWhereConditions(GetPostTableRequestDto dto) {
        BooleanBuilder whereConditions = new BooleanBuilder();

        // 포스팅 id로 검색 조건 추가
        Long id = dto.getPostIdAsLong();

        if(id != null) {
            whereConditions.and(qPostEntity.id.eq(id));
        }

        return whereConditions;
    }

    /**
     * 총 포스팅(피드) 수 카운트
     */
    private int countTotalPosts(BooleanBuilder whereConditions) {
        Long count = queryFactory
            .select(qPostEntity.count())
            .from(qPostEntity)
            .join(qUserEntity).on(qPostEntity.author.id.eq(qUserEntity.id))
            .where(whereConditions)
            .fetchOne();

        return count != null ? count.intValue() : 0;
    }
}