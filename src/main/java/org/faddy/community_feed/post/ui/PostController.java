package org.faddy.community_feed.post.ui;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.common.idempotency.annotation.Idempotent;
import org.faddy.community_feed.common.principal.AuthPrincipal;
import org.faddy.community_feed.common.principal.UserPrincipal;
import org.faddy.community_feed.common.ui.Response;
import org.faddy.community_feed.post.application.dto.CreatePostRequestDto;
import org.faddy.community_feed.post.application.dto.LikeRequestDto;
import org.faddy.community_feed.post.application.dto.UpdatePostRequestDto;
import org.faddy.community_feed.post.application.service.PostService;
import org.faddy.community_feed.post.domain.Post;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Long> createPost(@RequestBody CreatePostRequestDto dto,
        @AuthPrincipal UserPrincipal principal) {
        Post post = postService.createPost(principal.getUserId() , dto);
        return Response.ok(post.getId());
    }

    @PatchMapping("/{postId}")
    public Response<Long> updatePost(@PathVariable(name = "postId") Long postId,
        @RequestBody UpdatePostRequestDto dto,
        @AuthPrincipal UserPrincipal principal) {
        Post post = postService.updatePost(postId, principal.getUserId(), dto);
        return Response.ok(post.getId());
    }

    @Idempotent
    @PostMapping("/like")
    public Response<Void> likePost(@RequestBody LikeRequestDto dto,
        @AuthPrincipal UserPrincipal principal) {
        postService.likePost(principal.getUserId(), dto.targetId());
        return Response.ok(null);
    }

    @PostMapping("/unlike")
    public Response<Void> unlikePost(@RequestBody LikeRequestDto dto,
        @AuthPrincipal UserPrincipal principal) {
        postService.unlikePost(principal.getUserId(), dto.targetId());
        return Response.ok(null);
    }
}