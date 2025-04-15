package org.fastcampus.community_feed.user.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.common.ui.Response;
import org.fastcampus.community_feed.user.application.UserService;
import org.fastcampus.community_feed.user.application.dto.GetUserListResponseDto;
import org.fastcampus.community_feed.user.application.dto.GetUserResponseDto;
import org.fastcampus.community_feed.user.repository.jpa.JpaUserListQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JpaUserListQueryRepository userListEntityQuery;

    @GetMapping("/{userId}")
    public Response<GetUserResponseDto> getUserResponse(@PathVariable(name="userId") Long id) {
        return Response.ok(userService.getUserProfile(id));
    }

    @GetMapping("/{userId}/follower")
    public Response<List<GetUserListResponseDto>> getFollowerList(@PathVariable(name="userId") Long id) {
        return Response.ok(userListEntityQuery.getFollowerList(id));
    }

    @GetMapping("/{userId}/following")
    public Response<List<GetUserListResponseDto>> getFollowingList(@PathVariable(name="userId") Long id) {
        return Response.ok(userListEntityQuery.getFollowingList(id));
    }
}
