package org.faddy.User.application;

import org.faddy.User.application.dto.CreateUserRequestDto;
import org.faddy.User.application.interfae.UserRepository;
import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 유저 생성
    public User createUser(CreateUserRequestDto dto) {
        UserInfo userInfo = new UserInfo(dto.name() , dto.profileImageUrl());
        User user = new User(null , userInfo);
        User newUser = this.userRepository.save(user);

        if (newUser == null) {
            throw new IllegalArgumentException("유저 생성 오류 Error Code 4020");
        }
        return newUser;
    }

    public User getUser(Long id) {

        return this.userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
