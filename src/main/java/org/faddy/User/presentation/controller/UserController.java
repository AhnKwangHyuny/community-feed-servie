package org.faddy.User.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.User.application.dto.CreateUserRequestDto;
import org.faddy.User.application.dto.GetUserListResponseDto;
import org.faddy.User.application.dto.GetUserResponseDto;
import org.faddy.User.application.interfaces.UserService;
import org.faddy.User.domain.User;
import org.faddy.User.infrastructure.repo.jpa.JpaUserListQueryRepository;
import org.faddy.common.presentation.ui.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JpaUserListQueryRepository userListEntityQuery;

    /**
     * 새로운 사용자를 생성
     *
     * @param dto 사용자 생성에 필요한 데이터 전송 객체
     * @return 생성된 사용자의 ID를 포함한 응답 객체
     */
    @PostMapping  // POST /users
    public Response<Long> createUser(@RequestBody CreateUserRequestDto dto) {
        User user = userService.createUser(dto);  // 사용자 생성 서비스 호출
        return Response.ok(user.getId());  // 생성된 사용자 ID 반환
    }

    /**
     * 특정 사용자의 프로필 정보를 조회
     *
     * @param id 조회할 사용자의 ID
     * @return 사용자 프로필 정보를 포함한 응답 객체
     */
    @GetMapping("/{userId}")  // GET /users/{userId}
    public Response<GetUserResponseDto> getUserResponse(@PathVariable(name="userId") Long id) {
        return Response.ok(userService.getUserProfile(id));  // 사용자 프로필 정보 반환
    }

    /**
     * 특정 사용자의 팔로워 목록을 조회
     * 해당 사용자를 팔로우하는 사용자 목록을 반환
     *
     * @param id 팔로워 목록을 조회할 사용자의 ID
     * @return 팔로워 목록을 포함한 응답 객체
     */
    @GetMapping("/{userId}/follower")  // GET /users/{userId}/follower
    public Response<List<GetUserListResponseDto>> getFollowerList(@PathVariable(name="userId") Long id) {
        return Response.ok(userListEntityQuery.getFollowingList(id));  // 팔로워 목록 반환
    }

    /**
     * 특정 사용자의 팔로잉 목록을 조회
     * 해당 사용자가 팔로우하는 사용자 목록을 반환
     *
     * @param id 팔로잉 목록을 조회할 사용자의 ID
     * @return 팔로잉 목록을 포함한 응답 객체
     */
    @GetMapping("/{userId}/following")  // GET /users/{userId}/following
    public Response<List<GetUserListResponseDto>> getFollowingList(@PathVariable(name="userId") Long id) {
        return Response.ok(userListEntityQuery.getFollowingList(id));  // 팔로잉 목록 반환
    }


}
