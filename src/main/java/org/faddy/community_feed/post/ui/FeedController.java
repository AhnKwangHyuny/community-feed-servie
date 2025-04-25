package org.faddy.community_feed.post.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.principal.AuthPrincipal;
import org.faddy.community_feed.common.principal.UserPrincipal;
import org.faddy.community_feed.common.ui.Response;
import org.faddy.community_feed.post.application.dto.response.GetPostContent2ResponseDto;
import org.faddy.community_feed.post.application.dto.response.GetPostContentResponseDto;
import org.faddy.community_feed.post.repository.post_queue.interfaces.UserPostQueueQueryRepository;
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
     * 메인 피드 게시글 조회 - 최신순 기본 정렬
     * 무한 스크롤을 위한 커서 기반 페이징 적용
     */
    @GetMapping
    public Response<List<GetPostContent2ResponseDto>> getPostFeed(
        @RequestParam(required = false) Long lastContentId,
        @RequestParam(required = false, defaultValue = "latest") String sort,
        @AuthPrincipal UserPrincipal principal) {

        Long userId = principal != null ? principal.getUserId() : null;
        log.info("게시글 피드 조회: lastContentId={}, sort={}, userId={}", lastContentId, sort, userId);

        List<GetPostContent2ResponseDto> posts = postFeedService.getPostsBySort(lastContentId, sort, userId);

        return Response.ok(posts);
    }

    /**
     * 인기 게시글 조회 (좋아요순)
     */
    @GetMapping("/popular")
    public Response<List<GetPostContent2ResponseDto>> getPopularPosts(
        @RequestParam(required = false) Long lastContentId,
        @AuthPrincipal UserPrincipal principal) {

        Long userId = principal != null ? principal.getUserId() : null;
        log.info("인기 게시글 조회: lastContentId={}, userId={}", lastContentId, userId);

        List<GetPostContent2ResponseDto> posts = postFeedService.getPopularPosts(lastContentId, userId);

        return Response.ok(posts);
    }

    /**
     * 추천 게시글 조회
     */
    @GetMapping("/recommended")
    public Response<List<GetPostContent2ResponseDto>> getRecommendedPosts(
        @RequestParam(required = false) Long lastContentId,
        @AuthPrincipal UserPrincipal principal) {

        Long userId = principal != null ? principal.getUserId() : null;
        log.info("추천 게시글 조회: lastContentId={}, userId={}", lastContentId, userId);

        List<GetPostContent2ResponseDto> posts = postFeedService.getRecommendedPosts(lastContentId, userId);

        return Response.ok(posts);
    }

    /**
     * 내 게시글 조회 (로그인 필수)
     */
    @GetMapping("/my")
    public Response<List<GetPostContent2ResponseDto>> getMyPosts(
        @RequestParam(required = false) Long lastContentId,
        @AuthPrincipal UserPrincipal principal) {

        if (principal == null) {
            return Response.error("로그인이 필요합니다.");
        }

        Long userId = principal.getUserId();
        log.info("내 게시글 조회: lastContentId={}, userId={}", lastContentId, userId);

        List<GetPostContent2ResponseDto> posts = postFeedService.getMyPosts(lastContentId, userId);

        return Response.ok(posts);
    }

    /**
     *  추후 도입
     * */

    //    private final UserPostQueueQueryRepository userPostQueueQueryRepository;
//
//    @GetMapping
//    public Response<List<GetPostContentResponseDto>> getPostFeedList(@AuthPrincipal UserPrincipal user, Long lastContentId) {
//        List<GetPostContentResponseDto> contentResponse = userPostQueueQueryRepository.getContentResponse(user.getUserId(), lastContentId);
//        return Response.ok(contentResponse);
//    }

}
