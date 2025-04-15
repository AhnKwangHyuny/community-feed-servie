package org.fastcampus.community_feed.post.ui;

import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.common.principal.AuthPrincipal;
import org.fastcampus.community_feed.common.principal.UserPrincipal;
import org.fastcampus.community_feed.common.ui.Response;
import org.fastcampus.community_feed.post.application.PostService;
import org.fastcampus.community_feed.post.application.dto.CreatePostRequestDto;
import org.fastcampus.community_feed.post.application.dto.LikeRequestDto;
import org.fastcampus.community_feed.post.application.dto.UpdatePostRequestDto;
import org.fastcampus.community_feed.post.domain.Post;
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
    public Response<Long> createPost(@RequestBody CreatePostRequestDto dto) {
        Post post = postService.createPost(dto);
        return Response.ok(post.getId());
    }

    @PatchMapping("/{postId}")
    public Response<Long> updatePost(@PathVariable(name = "postId") Long postId, @RequestBody UpdatePostRequestDto dto) {
        Post post = postService.updatePost(postId, dto);
        return Response.ok(post.getId());
    }

    @PostMapping("/like")
    public Response<Void> likePost(@RequestBody LikeRequestDto dto) {
        postService.likePost(dto);
        return Response.ok(null);
    }

    @PostMapping("/unlike")
    public Response<Void> unlikePost(@RequestBody LikeRequestDto dto) {
        postService.unlikePost(dto);
        return Response.ok(null);
    }
}
