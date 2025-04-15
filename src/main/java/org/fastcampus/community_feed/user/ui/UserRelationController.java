package org.fastcampus.community_feed.user.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.common.ui.Response;
import org.fastcampus.community_feed.user.application.UserRelationService;
import org.fastcampus.community_feed.user.application.dto.FollowUserRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/relation")
@RequiredArgsConstructor
public class UserRelationController {

    private final UserRelationService userRelationService;

    @PostMapping("/follow")
    public Response<Void> followUser(@RequestBody @Valid FollowUserRequestDto dto) {
        userRelationService.followUser(dto);
        return Response.ok(null);
    }

    @PostMapping("/unfollow")
    public Response<Void> unfollowUser(@RequestBody @Valid FollowUserRequestDto dto) {
        userRelationService.unfollowUser(dto);
        return Response.ok(null);
    }
}
