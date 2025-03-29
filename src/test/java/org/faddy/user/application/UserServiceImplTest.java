package org.faddy.user.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.faddy.User.application.service.UserServiceImpl;
import org.faddy.User.application.dto.CreateUserRequestDto;
import org.faddy.User.application.interfaces.UserRepository;
import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.faddy.user.repository.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceImplTest {

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private UserInfo userInfo;

    public UserServiceImplTest() {
        this.userRepository = new FakeUserRepository();
        this.userServiceImpl = new UserServiceImpl(this.userRepository);
    }

    @BeforeEach
    void setUp() {
        this.userInfo = new UserInfo("ahn" , "");
    }

    @Test
    void givenCreateUserDto_whenCreateUser_thenSaveUser() {
        // given
        String name = "ahn";
        String profileImageUrl = "";
        CreateUserRequestDto dto = new CreateUserRequestDto(name, profileImageUrl);

        //when
        User newUser = userServiceImpl.createUser(dto);

        //then
        assertEquals(1L , newUser.getId());
    }

    @Test
    void givenCreateUserRequestDto_whenCreateUser_thanCanFindUser() {
        // given
        CreateUserRequestDto dto = new CreateUserRequestDto(userInfo.getName() , userInfo.getProfileImageUrl());

        //when
        User newUser = userServiceImpl.createUser(dto);
        User foundUser = userServiceImpl.getUser(newUser.getId());

        //then
        assertEquals(newUser.getId(), foundUser.getId());
    }

}
