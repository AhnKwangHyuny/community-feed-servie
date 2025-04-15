package org.fastcampus.community_feed.user.application;

import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.user.application.dto.FollowUserRequestDto;
import org.fastcampus.community_feed.user.application.interfaces.UserRelationRepository;
import org.fastcampus.community_feed.user.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRelationService {

    private final UserRelationRepository userRelationRepository;
    private final UserService userService;

    public void followUser(FollowUserRequestDto dto) {
        User user = userService.getUser(dto.userId());
        User targetUser = userService.getUser(dto.targetUserId());

        if (userRelationRepository.isAlreadyFollow(user, targetUser)) {
            throw new IllegalArgumentException("Already followed");
        }

        user.follow(targetUser);

        userRelationRepository.save(user, targetUser);
    }

    public void unfollowUser(FollowUserRequestDto dto) {
        User user = userService.getUser(dto.userId());
        User targetUser = userService.getUser(dto.targetUserId());

        if (!userRelationRepository.isAlreadyFollow(user, targetUser)) {
            throw new IllegalArgumentException("Not followed yet");
        }

        user.unfollow(targetUser);

        userRelationRepository.delete(user, targetUser);
    }
}
