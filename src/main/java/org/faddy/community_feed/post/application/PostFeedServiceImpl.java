package org.faddy.community_feed.post.application;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.post.application.dto.response.GetPostContent2ResponseDto;
import org.faddy.community_feed.post.application.service.PostFeedService;
import org.faddy.community_feed.post.domain.enumeration.SortCriteria;
import org.faddy.community_feed.post.repository.post_queue.interfaces.PostQueryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostFeedServiceImpl implements PostFeedService {

    private final PostQueryRepository postQueryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY_PREFIX = "feed:";
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Value("${app.feed.cache.enabled:true}")
    private boolean cacheEnabled;

    @Value("${app.feed.cache.ttl-minutes:5}")
    private int cacheTtlMinutes;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetPostContent2ResponseDto> getPostsBySort(Long lastContentId, String sort, Long userId) {
        StopWatch stopWatch = new StopWatch("getPostsBySort");
        stopWatch.start();

        try {
            SortCriteria sortCriteria = SortCriteria.fromString(sort);
            log.info("정렬 기준으로 게시물 조회: criteria={}, lastContentId={}, userId={}", sortCriteria, lastContentId, userId);

            // 첫 페이지 비로그인 사용자는 캐싱 적용
            if (cacheEnabled && lastContentId == null && userId == null) {
                String cacheKey = CACHE_KEY_PREFIX + sortCriteria.getValue();

                @SuppressWarnings("unchecked")
                List<GetPostContent2ResponseDto> cachedResult =
                    (List<GetPostContent2ResponseDto>) redisTemplate.opsForValue().get(cacheKey);

                if (cachedResult != null) {
                    log.debug("캐시에서 피드 데이터 조회됨: {}", cacheKey);
                    return cachedResult;
                }
            }

            // 캐시 미스 또는 캐싱 대상이 아닌 경우 DB 조회
            List<GetPostContent2ResponseDto> result;

            switch (sortCriteria) {
                case LATEST:
                    result = postQueryRepository.findLatestPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
                    break;
                case OLDEST:
                    result = postQueryRepository.findOldestPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
                    break;
                case POPULAR:
                    result = postQueryRepository.findPopularPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
                    break;
                case VIEWS:
                    result = postQueryRepository.findMostViewedPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
                    break;
                case COMMENTS:
                    result = postQueryRepository.findMostCommentedPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
                    break;
                default:
                    result = postQueryRepository.findLatestPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
            }

            // 첫 페이지 비로그인 사용자 결과 캐싱
            if (cacheEnabled && lastContentId == null && userId == null) {
                String cacheKey = CACHE_KEY_PREFIX + sortCriteria.getValue();
                redisTemplate.opsForValue().set(cacheKey, result, cacheTtlMinutes, TimeUnit.MINUTES);
                log.debug("피드 데이터 캐시 저장됨: {}", cacheKey);
            }

            return result;

        } catch (Exception e) {
            log.error("피드 조회 중 오류 발생", e);
            throw new RuntimeException("피드 조회 중 오류가 발생했습니다", e);
        } finally {
            stopWatch.stop();
            log.debug("피드 조회 소요시간: {}ms", stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetPostContent2ResponseDto> getPopularPosts(Long lastContentId, Long userId) {
        log.info("인기 게시물 조회: lastContentId={}, userId={}", lastContentId, userId);

        // 첫 페이지 비로그인 사용자는 캐싱 적용
        if (cacheEnabled && lastContentId == null && userId == null) {
            String cacheKey = CACHE_KEY_PREFIX + "popular";

            @SuppressWarnings("unchecked")
            List<GetPostContent2ResponseDto> cachedResult =
                (List<GetPostContent2ResponseDto>) redisTemplate.opsForValue().get(cacheKey);

            if (cachedResult != null) {
                log.debug("캐시에서 인기 게시물 데이터 조회됨");
                return cachedResult;
            }

            // 캐시 미스 시 DB 조회 후 캐싱
            List<GetPostContent2ResponseDto> result = postQueryRepository.findPopularPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
            redisTemplate.opsForValue().set(cacheKey, result, cacheTtlMinutes, TimeUnit.MINUTES);
            return result;
        }

        // 캐싱 대상이 아닌 경우 직접 DB 조회
        return postQueryRepository.findPopularPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetPostContent2ResponseDto> getRecommendedPosts(Long lastContentId, Long userId) {
        log.info("추천 게시물 조회: lastContentId={}, userId={}", lastContentId, userId);

        // 첫 페이지 비로그인 사용자는 캐싱 적용
        if (cacheEnabled && lastContentId == null && userId == null) {
            String cacheKey = CACHE_KEY_PREFIX + "recommended";

            @SuppressWarnings("unchecked")
            List<GetPostContent2ResponseDto> cachedResult =
                (List<GetPostContent2ResponseDto>) redisTemplate.opsForValue().get(cacheKey);

            if (cachedResult != null) {
                log.debug("캐시에서 추천 게시물 데이터 조회됨");
                return cachedResult;
            }

            // 캐시 미스 시 DB 조회 후 캐싱
            List<GetPostContent2ResponseDto> result = postQueryRepository.findRecommendedPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
            redisTemplate.opsForValue().set(cacheKey, result, cacheTtlMinutes, TimeUnit.MINUTES);
            return result;
        }

        // 캐싱 대상이 아닌 경우 직접 DB 조회
        return postQueryRepository.findRecommendedPosts(lastContentId, DEFAULT_PAGE_SIZE, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetPostContent2ResponseDto> getMyPosts(Long lastContentId, Long userId) {
        if (userId == null) {
            log.warn("사용자 ID 없이 내 게시물 조회 요청");
            return List.of(); // 사용자 ID가 없으면 빈 목록 반환
        }

        log.info("내 게시물 조회: lastContentId={}, userId={}", lastContentId, userId);
        return postQueryRepository.findPostsByUserId(userId, lastContentId, DEFAULT_PAGE_SIZE);
    }

    /**
     * 캐시 무효화 - 게시물 작성/수정/삭제 시 호출
     * <p>
     * 게시물 데이터가 변경되면 캐시된 피드 데이터를 무효화합니다.
     * 이는 사용자가 항상 최신 데이터를 볼 수 있도록 보장합니다.
     * </p>
     */
    public void invalidateFeedCache() {
        if (!cacheEnabled) {
            return;
        }

        log.info("피드 캐시 무효화 실행");
        for (SortCriteria criteria : SortCriteria.values()) {
            String cacheKey = CACHE_KEY_PREFIX + criteria.getValue();
            redisTemplate.delete(cacheKey);
        }

        // 특수 피드 캐시도 함께 무효화
        redisTemplate.delete(CACHE_KEY_PREFIX + "popular");
        redisTemplate.delete(CACHE_KEY_PREFIX + "recommended");

        log.info("피드 캐시 무효화 완료");
    }
}