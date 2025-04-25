package org.faddy.community_feed.post.ui;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.principal.AuthPrincipal;
import org.faddy.community_feed.common.principal.UserPrincipal;
import org.faddy.community_feed.common.ui.Response;
import org.faddy.community_feed.post.application.dto.response.PostDetailResponseDto;
import org.faddy.community_feed.post.application.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/detail")
@Slf4j
@RequiredArgsConstructor
public class PostDetailQueryController {

    private final PostService postService;

    /**
     * 게시글 상세 조회 API
     */
    @GetMapping("/{postId}")
    public Response<PostDetailResponseDto> getPostDetail(
        @PathVariable Long postId,
        @AuthPrincipal UserPrincipal principal) {

        log.info("Post detail request received for postId: {}", postId);

        Long userId = principal != null ? principal.getUserId() : null;
        PostDetailResponseDto postDetail = postService.getPostDetail(postId, userId);

        log.info("Post detail response sent for postId: {}", postId);

        return Response.ok(postDetail);
    }
}
