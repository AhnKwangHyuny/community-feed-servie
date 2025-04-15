package org.fastcampus.community_feed.user.application.dto;

import jakarta.validation.constraints.NotNull;

public record FollowUserRequestDto
        (@NotNull Long userId,
        @NotNull Long targetUserId) {
}
