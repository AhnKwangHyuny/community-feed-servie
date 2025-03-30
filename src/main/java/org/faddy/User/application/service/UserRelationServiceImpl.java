package org.faddy.User.application.service;

import lombok.RequiredArgsConstructor;
import org.faddy.User.application.dto.FollowUserRequestDto;
import org.faddy.User.application.interfaces.UserRelationRepository;
import org.faddy.User.application.interfaces.UserRelationService;
import org.faddy.User.application.interfaces.UserRepository;
import org.faddy.User.application.interfaces.UserService;
import org.faddy.User.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRelationServiceImpl implements UserRelationService {
    private final UserService userService;
    private final UserRelationRepository userRelationRepository;
    private final UserRepository userRepository;

    @Override
    public void follow(FollowUserRequestDto dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(IllegalArgumentException::new);
        User targetUser = userRepository.findById(dto.targetUserId()).orElseThrow(IllegalArgumentException::new);

        //isFollow?
        if(userRelationRepository.isFollowUser(user, targetUser)) {
            throw new IllegalArgumentException("해당 유저는 이미 팔로우된 상태 입니다.");
        }

        userRelationRepository.save(user, targetUser);
    }

    @Override
    public void unFollow(FollowUserRequestDto dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(IllegalArgumentException::new);
        User targetUser = userRepository.findById(dto.targetUserId()).orElseThrow(IllegalArgumentException::new);

        //isFollow?
        if(!userRelationRepository.isFollowUser(user, targetUser)) {
            throw new IllegalArgumentException("해당 유저는 이미 팔로우된 상태 입니다.");
        }

        userRelationRepository.delete(user, targetUser);
    }

}
