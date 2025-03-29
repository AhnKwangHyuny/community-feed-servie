package org.faddy.User.application.dto;

import org.faddy.User.domain.User;

public record GetUserResponseDto(Long id, String name, String profileImage, Integer followingCount, Integer followerCount) {

    public GetUserResponseDto(User user) {
        this(user.getId(), user.getUserName(), user.getUserProfileImageUrl(), user.getFollowingCount(), user.getFollowerCount());
    }

}
