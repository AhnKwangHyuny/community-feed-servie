package org.faddy.User.application.interfaces;

import org.faddy.User.application.dto.FollowUserRequestDto;

public interface UserRelationService {
    /**
     * 사용자가 다른 사용자를 팔로우합니다.
     *
     * @param dto 팔로우 요청 정보(사용자 ID와 대상 사용자 ID)
     * @throws IllegalArgumentException 이미 팔로우된 상태이거나 사용자가 존재하지 않는 경우
     */
    void follow(FollowUserRequestDto dto);

    /**
     * 사용자가 다른 사용자를 언팔로우합니다.
     *
     * @param dto 언팔로우 요청 정보(사용자 ID와 대상 사용자 ID)
     * @throws IllegalArgumentException 팔로우되지 않은 상태이거나 사용자가 존재하지 않는 경우
     */
    void unFollow(FollowUserRequestDto dto);

}
