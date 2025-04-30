package org.faddy.community_feed.post.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.faddy.community_feed.post.repository.entity.like.QLikeEntity.likeEntity;
import static org.faddy.community_feed.post.repository.entity.post.QPostEntity.postEntity;
import static org.faddy.community_feed.user.repository.entity.QUserEntity.userEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.post.application.dto.response.GetPostContent2ResponseDto;
import org.faddy.community_feed.post.domain.enumeration.PostPublicationState;
import org.faddy.community_feed.post.repository.post_queue.interfaces.PostQueryRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;


@Repository
@RequiredArgsConstructor
@Slf4j
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String POST_TARGET_TYPE = "POST"; // 좋아요 대상 타입 상수

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findLatestPosts(Long lastContentId, int pageSize, Long userId) {
        StopWatch stopWatch = new StopWatch("findLatestPosts");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("최신순 게시물 조회: lastContentId={}, pageSize={}, userId={}", lastContentId, sanitizedPageSize, userId);

            // 기본 정보 조회
            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    isPublic(),
                    idLessThan(lastContentId)
                )
                .orderBy(postEntity.id.desc()) // 최신순 (ID 내림차순)
                .limit(sanitizedPageSize)
                .fetch();

            // 이미지 정보 보강
            if (!result.isEmpty()) {
                List<Long> postIds = result.stream()
                    .map(GetPostContent2ResponseDto::getId)
                    .collect(Collectors.toList());
                enrichPostsWithImages(result, postIds);
            }

            log.debug("최신순 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("최신순 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findOldestPosts(Long lastContentId, int pageSize, Long userId) {
        StopWatch stopWatch = new StopWatch("findOldestPosts");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("오래된순 게시물 조회: lastContentId={}, pageSize={}, userId={}", lastContentId, sanitizedPageSize, userId);

            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    isPublic(),
                    idGreaterThan(lastContentId)
                )
                .orderBy(postEntity.id.asc()) // 오래된순 (ID 오름차순)
                .limit(sanitizedPageSize)
                .fetch();

            log.debug("오래된순 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("오래된순 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findPopularPosts(Long lastContentId, int pageSize, Long userId) {
        StopWatch stopWatch = new StopWatch("findPopularPosts");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("인기순 게시물 조회: lastContentId={}, pageSize={}, userId={}", lastContentId, sanitizedPageSize, userId);

            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    isPublic(),
                    likesLessThanOrIdLessThan(lastContentId)
                )
                .orderBy(postEntity.likeCount.desc(), postEntity.id.desc()) // 좋아요 순, 같은 경우 최신순
                .limit(sanitizedPageSize)
                .fetch();

            log.debug("인기순 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("인기순 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findMostViewedPosts(Long lastContentId, int pageSize, Long userId) {
        StopWatch stopWatch = new StopWatch("findMostViewedPosts");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("조회수순 게시물 조회: lastContentId={}, pageSize={}, userId={}", lastContentId, sanitizedPageSize, userId);

            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    isPublic(),
                    viewsLessThanOrIdLessThan(lastContentId)
                )
                .orderBy(postEntity.viewCounter.desc(), postEntity.id.desc()) // 조회수 순, 같은 경우 최신순
                .limit(sanitizedPageSize)
                .fetch();

            log.debug("조회수순 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("조회수순 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findMostCommentedPosts(Long lastContentId, int pageSize, Long userId) {
        StopWatch stopWatch = new StopWatch("findMostCommentedPosts");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("댓글순 게시물 조회: lastContentId={}, pageSize={}, userId={}", lastContentId, sanitizedPageSize, userId);

            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    isPublic(),
                    commentsLessThanOrIdLessThan(lastContentId)
                )
                .orderBy(postEntity.commentCounter.desc(), postEntity.id.desc()) // 댓글 순, 같은 경우 최신순
                .limit(sanitizedPageSize)
                .fetch();

            log.debug("댓글순 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("댓글순 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findRecommendedPosts(Long lastContentId, int pageSize, Long userId) {
        StopWatch stopWatch = new StopWatch("findRecommendedPosts");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("추천순 게시물 조회: lastContentId={}, pageSize={}, userId={}", lastContentId, sanitizedPageSize, userId);

            // 추천 알고리즘: 좋아요(3) + 조회수(1) + 댓글 수(2)의 가중치 합산
            NumberExpression<Long> recommendScore = calculateRecommendScore();

            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    isPublic(),
                    recommendScoreLessThanOrIdLessThan(lastContentId, recommendScore)
                )
                .orderBy(recommendScore.desc(), postEntity.id.desc()) // 추천 점수 순, 같은 경우 최신순
                .limit(sanitizedPageSize)
                .fetch();

            log.debug("추천순 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("추천순 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPostContent2ResponseDto> findPostsByUserId(Long userId, Long lastContentId, int pageSize) {
        if (userId == null) {
            log.warn("사용자 ID 없이 내 게시물 조회 요청");
            return List.of();
        }

        StopWatch stopWatch = new StopWatch("findPostsByUserId");
        stopWatch.start("query-execution");

        try {
            int sanitizedPageSize = sanitizePageSize(pageSize);
            log.debug("사용자별 게시물 조회: userId={}, lastContentId={}, pageSize={}", userId, lastContentId, sanitizedPageSize);

            List<GetPostContent2ResponseDto> result = queryFactory
                .select(getPostProjection(userId))
                .from(postEntity)
                .leftJoin(postEntity.author, userEntity)
                .where(
                    authorIdEquals(userId),
                    idLessThan(lastContentId)
                )
                .orderBy(postEntity.id.desc()) // 최신순
                .limit(sanitizedPageSize)
                .fetch();

            log.debug("사용자별 게시물 조회 결과: {} 건", result.size());
            return result;

        } catch (Exception e) {
            log.error("사용자별 게시물 조회 중 오류 발생", e);
            throw new RuntimeException("게시물 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            logExecutionTime(stopWatch);
        }
    }

    // Where 조건 메서드
    /**
     * 게시물 DTO 프로젝션 생성
     */
    private com.querydsl.core.types.Expression<GetPostContent2ResponseDto> getPostProjection(Long userId) {
        return Projections.fields(GetPostContent2ResponseDto.class,
            // 상위 클래스 필드들 (GetContentResponseDto)
            postEntity.id.as("id"),
            postEntity.content.as("content"),
            postEntity.author.id.as("userId"),
            postEntity.author.name.as("userName"),
            postEntity.author.profileImage.as("userProfileImage"),
            postEntity.regDt.as("createdAt"),
            postEntity.updDt.as("updatedAt"),
            postEntity.likeCount.as("likeCount"),
            isLikedByUser(userId).as("isLikedByMe"),

            // GetPostContent2ResponseDto 고유 필드들 (이미지 관련 필드 제외)
            postEntity.commentCounter.as("commentCount"),
            postEntity.viewCounter.as("viewCount"),
            postEntity.state.as("state")
            // thumbnailUrl과 images는 별도 처리
        );
    }
    /**
     * 추천 점수 계산식
     * 좋아요(3) + 조회수(1) + 댓글 수(2)의 가중치 합산
     */
    private NumberExpression<Long> calculateRecommendScore() {
        return postEntity.likeCount.longValue().multiply(3L)
            .add(postEntity.viewCounter.longValue().multiply(1L))
            .add(postEntity.commentCounter.longValue().multiply(2L));
    }

    /**
     * 공개 상태 게시물 필터링
     */
    private BooleanExpression isPublic() {
        return postEntity.state.eq(PostPublicationState.PUBLIC);
    }

    /**
     * 특정 작성자의 게시물 필터링
     */
    private BooleanExpression authorIdEquals(Long userId) {
        return postEntity.author.id.eq(userId);
    }

    /**
     * ID 기준 커서 페이징 (최신순)
     */
    private BooleanExpression idLessThan(Long lastContentId) {
        return lastContentId != null ? postEntity.id.lt(lastContentId) : null;
    }

    /**
     * ID 기준 커서 페이징 (오래된순)
     */
    private BooleanExpression idGreaterThan(Long lastContentId) {
        return lastContentId != null ? postEntity.id.gt(lastContentId) : null;
    }

    /**
     * 좋아요 수 기준 커서 페이징
     */
    private BooleanExpression likesLessThanOrIdLessThan(Long lastContentId) {
        if (lastContentId == null) {
            return null;
        }

        Integer lastLikeCount = getLastPropertyValue(lastContentId, postEntity.likeCount);
        if (lastLikeCount == null) {
            return null;
        }

        return postEntity.likeCount.lt(lastLikeCount)
            .or(postEntity.likeCount.eq(lastLikeCount).and(postEntity.id.lt(lastContentId)));
    }

    /**
     * 조회수 기준 커서 페이징
     */
    private BooleanExpression viewsLessThanOrIdLessThan(Long lastContentId) {
        if (lastContentId == null) {
            return null;
        }

        Integer lastViewCount = getLastPropertyValue(lastContentId, postEntity.viewCounter);
        if (lastViewCount == null) {
            return null;
        }

        return postEntity.viewCounter.lt(lastViewCount)
            .or(postEntity.viewCounter.eq(lastViewCount).and(postEntity.id.lt(lastContentId)));
    }

    /**
     * 댓글수 기준 커서 페이징
     */
    private BooleanExpression commentsLessThanOrIdLessThan(Long lastContentId) {
        if (lastContentId == null) {
            return null;
        }

        Integer lastCommentCount = getLastPropertyValue(lastContentId, postEntity.commentCounter);
        if (lastCommentCount == null) {
            return null;
        }

        return postEntity.commentCounter.lt(lastCommentCount)
            .or(postEntity.commentCounter.eq(lastCommentCount).and(postEntity.id.lt(lastContentId)));
    }

    /**
     * 추천 점수 기반 커서 페이징
     */
    private BooleanExpression recommendScoreLessThanOrIdLessThan(Long lastContentId, NumberExpression<Long> recommendScore) {
        if (lastContentId == null) {
            return null;
        }

        Long lastRecommendScore = getLastRecommendScore(lastContentId);
        if (lastRecommendScore == null) {
            return null;
        }

        return recommendScore.lt(lastRecommendScore)
            .or(recommendScore.eq(lastRecommendScore).and(postEntity.id.lt(lastContentId)));
    }

    /**
     * 좋아요 여부 서브쿼리 - LikeId 복합키를 사용하는 버전
     *
     * @param userId 사용자 ID
     * @return 현재 사용자가 게시물에 좋아요를 눌렀는지 여부
     */
    private BooleanExpression isLikedByUser(Long userId) {
        if (userId == null) {
            return postEntity.id.eq(postEntity.id).and(postEntity.id.ne(postEntity.id)); // 항상 false
        }

        return queryFactory
            .selectOne()
            .from(likeEntity)
            .where(
                likeEntity.id.targetId.eq(postEntity.id),
                likeEntity.id.userId.eq(userId),
                likeEntity.id.targetType.eq(POST_TARGET_TYPE)
            )
            .exists();
    }

    /**
     * 게시물의 특정 속성 값 조회
     */
    private <T> T getLastPropertyValue(Long postId, com.querydsl.core.types.Expression<T> property) {
        return Optional.ofNullable(
            queryFactory
                .select(property)
                .from(postEntity)
                .where(postEntity.id.eq(postId))
                .fetchOne()
        ).orElse(null);
    }

    /**
     * 마지막 게시물의 추천 점수 계산
     */
    private Long getLastRecommendScore(Long lastContentId) {
        return queryFactory
            .select(
                postEntity.likeCount.longValue().multiply(3L)
                    .add(postEntity.viewCounter.longValue().coalesce(0L))
                    .add(postEntity.commentCounter.longValue().multiply(2L))
            )
            .from(postEntity)
            .where(postEntity.id.eq(lastContentId))
            .fetchOne();
    }

    /**
     * 페이지 크기 유효성 검사
     */
    private int sanitizePageSize(int pageSize) {
        if (pageSize <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    /**
     * 실행 시간 로깅
     */
    private void logExecutionTime(StopWatch stopWatch) {
        if (stopWatch.getTotalTimeMillis() > 1000) {
            log.warn("쿼리 성능 경고: {}ms 소요 ({})",
                stopWatch.getTotalTimeMillis(), stopWatch.getId());
        } else {
            log.debug("쿼리 소요시간: {}ms ({})",
                stopWatch.getTotalTimeMillis(), stopWatch.getId());
        }
    }

    /**
    * 이미지 정보 보강 메서드 추가
    * */

    private void enrichPostsWithImages(List<GetPostContent2ResponseDto> posts, List<Long> postIds) {
        try {
            // 메인 썸네일 URL 조회 (isMain=true)
            Map<Long, String> mainThumbnails = queryFactory
                .select(postEntity.id, postEntity.thumbnails.any().image.url)
                .from(postEntity)
                .join(postEntity.thumbnails)
                .join(postEntity.thumbnails.any().image)
                .where(
                    postEntity.id.in(postIds),
                    postEntity.thumbnails.any().isMain.isTrue()
                )
                .transform(groupBy(postEntity.id).as(postEntity.thumbnails.any().image.url));

            // 모든 이미지 URL 조회
            Map<Long, List<String>> allImages = queryFactory
                .select(postEntity.id, postEntity.thumbnails.any().image.url)
                .from(postEntity)
                .join(postEntity.thumbnails)
                .join(postEntity.thumbnails.any().image)
                .where(postEntity.id.in(postIds))
                .orderBy(postEntity.thumbnails.any().displayOrder.asc()) // 순서 정렬
                .transform(groupBy(postEntity.id).as(list(postEntity.thumbnails.any().image.url)));

            // DTO에 이미지 정보 설정
            for (GetPostContent2ResponseDto post : posts) {
                Long postId = post.getId();

                // 메인 썸네일 설정
                if (mainThumbnails.containsKey(postId)) {
                    post.setThumbnailUrl(mainThumbnails.get(postId));
                }

                // 모든 이미지 URL 설정
                if (allImages.containsKey(postId)) {
                    post.setImages(allImages.get(postId));
                } else {
                    post.setImages(Collections.emptyList());
                }
            }
        } catch (Exception e) {
            log.warn("이미지 정보 보강 중 오류 발생: {}", e.getMessage());
            // 이미지 오류로 전체 조회를 실패시키지 않음
        }
    }
}