package org.faddy.User.application.service;

import org.faddy.User.application.dto.CreateUserRequestDto;
import org.faddy.User.application.dto.GetUserResponseDto;
import org.faddy.User.application.interfaces.UserRepository;
import org.faddy.User.application.interfaces.UserService;
import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequestDto dto) {
        UserInfo userInfo = new UserInfo(dto.name() , dto.profileImageUrl());
        User user = new User(null , userInfo);
        User newUser = this.userRepository.save(user);

        if (newUser == null) {
            throw new IllegalArgumentException("유저 생성 오류 Error Code 4020");
        }
        return newUser;
    }

    @Override
    public User getUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }


    @Override
    public GetUserResponseDto getUserProfile(Long id) {
        User user = getUser(id);
        return new GetUserResponseDto(user);
    }
}