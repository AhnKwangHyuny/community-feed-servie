package org.faddy.community_feed.post.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.principal.AuthPrincipal;
import org.faddy.community_feed.common.principal.UserPrincipal;
import org.faddy.community_feed.common.ui.Response;
import org.faddy.community_feed.post.application.dto.response.GetPostContent2ResponseDto;
import org.faddy.community_feed.post.application.service.PostFeedService;
import org.faddy.community_feed.post.domain.enumeration.SortCriteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

    private final PostFeedService postFeedService;

    /**
     * 메인 피드 게시글 조회 API
     * <p>
     * 다양한 정렬 옵션을 지원하는 게시물 피드를 조회
     * 지원되는 정렬 옵션: latest(최신순, 기본값), oldest(오래된순), popular(좋아요순),
     * views(조회수순), comments(댓글순)
     * </p>
     */
    @GetMapping
    public Response<List<GetPostContent2ResponseDto>> getPostFeed(
        @RequestParam(required = false) Long lastContentId,
        @RequestParam(required = false, defaultValue = "latest") String sort,
        @AuthPrincipal UserPrincipal principal) {

        try {
            // 정렬 기준 유효성 검사
            try {
                SortCriteria.fromString(sort);
            } catch (IllegalArgumentException e) {
                log.warn("유효하지 않은 정렬 기준: {}", sort);
                return Response.error("유효하지 않은 정렬 기준입니다. 지원되는 값: latest, oldest, popular, views, comments");
            }

            Long userId = principal != null ? principal.getUserId() : null;
            log.info("게시글 피드 조회: lastContentId={}, sort={}, userId={}", lastContentId, sort, userId);

            List<GetPostContent2ResponseDto> posts = postFeedService.getPostsBySort(lastContentId, sort, userId);

            log.debug("게시글 피드 조회 결과: {} 건", posts.size());
            return Response.ok(posts);
        } catch (Exception e) {
            log.error("게시글 피드 조회 중 오류 발생", e);
            return Response.error("게시글 피드를 불러오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 인기 게시글 조회 API (좋아요순)
     */
    @GetMapping("/popular")
    public Response<List<GetPostContent2ResponseDto>> getPopularPosts(
        @RequestParam(required = false) Long lastContentId,
        @AuthPrincipal UserPrincipal principal) {

        try {
            Long userId = principal != null ? principal.getUserId() : null;
            log.info("인기 게시글 조회: lastContentId={}, userId={}", lastContentId, userId);

            List<GetPostContent2ResponseDto> posts = postFeedService.getPopularPosts(lastContentId, userId);

            log.debug("인기 게시글 조회 결과: {} 건", posts.size());
            return Response.ok(posts);
        } catch (Exception e) {
            log.error("인기 게시글 조회 중 오류 발생", e);
            return Response.error("인기 게시글을 불러오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 추천 게시글 조회 API
     * <p>
     * 기본 추천 알고리즘은 좋아요(×3) + 조회수(×1) + 댓글 수(×2) 가중치 합산입니다.
     * </p>
     */
    @GetMapping("/recommended")
    public Response<List<GetPostContent2ResponseDto>> getRecommendedPosts(
        @RequestParam(required = false) Long lastContentId,
        @AuthPrincipal UserPrincipal principal) {

        try {
            Long userId = principal != null ? principal.getUserId() : null;
            log.info("추천 게시글 조회: lastContentId={}, userId={}", lastContentId, userId);

            List<GetPostContent2ResponseDto> posts = postFeedService.getRecommendedPosts(lastContentId, userId);

            log.debug("추천 게시글 조회 결과: {} 건", posts.size());
            return Response.ok(posts);
        } catch (Exception e) {
            log.error("추천 게시글 조회 중 오류 발생", e);
            return Response.error("추천 게시글을 불러오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 내 게시글 조회 API (로그인 필수)
     */
    @GetMapping("/my")
    public Response<List<GetPostContent2ResponseDto>> getMyPosts(
        @RequestParam(required = false) Long lastContentId,
        @AuthPrincipal UserPrincipal principal) {

        try {
            if (principal == null) {
                log.warn("비로그인 사용자의 내 게시글 조회 시도");
                return Response.error("로그인이 필요합니다.");
            }

            Long userId = principal.getUserId();
            log.info("내 게시글 조회: lastContentId={}, userId={}", lastContentId, userId);

            List<GetPostContent2ResponseDto> posts = postFeedService.getMyPosts(lastContentId, userId);

            log.debug("내 게시글 조회 결과: {} 건", posts.size());
            return Response.ok(posts);
        } catch (Exception e) {
            log.error("내 게시글 조회 중 오류 발생", e);
            return Response.error("내 게시글을 불러오는 중 오류가 발생했습니다.");
        }
    }
}