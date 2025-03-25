package org.faddy.User.application.dto;

import org.faddy.User.domain.User;

public record FollowUserRequestDto(Long userId , Long targetUserId) {

}
