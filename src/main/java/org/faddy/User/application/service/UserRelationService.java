package org.faddy.User.application.service;

import org.faddy.User.application.dto.FollowUserRequestDto;
import org.faddy.User.application.interfae.UserRelationRepository;
import org.faddy.User.application.interfae.UserRepository;
import org.faddy.User.domain.User;

public class UserRelationService {
    private final UserServiceImpl userServiceImpl;
    private final UserRelationRepository userRelationRepository;
    private final UserRepository userRepository;

    public UserRelationService(UserServiceImpl userServiceImpl,
        UserRelationRepository userRelationRepository, UserRepository userRepository) {
        this.userServiceImpl = userServiceImpl;
        this.userRelationRepository = userRelationRepository;
        this.userRepository = userRepository;
    }

    public void follow(FollowUserRequestDto dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(IllegalArgumentException::new);
        User targetUser = userRepository.findById(dto.targetUserId()).orElseThrow(IllegalArgumentException::new);

        //isFollow?
        if(userRelationRepository.isFollowUser(user, targetUser)) {
            throw new IllegalArgumentException("해당 유저는 이미 팔로우된 상태 입니다.");
        }

        userRelationRepository.save(user, targetUser);
    }

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
