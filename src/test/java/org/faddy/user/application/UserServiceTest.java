package org.faddy.user.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.faddy.User.application.UserService;
import org.faddy.User.application.dto.CreateUserRequestDto;
import org.faddy.User.application.interfae.UserRepository;
import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.faddy.user.repository.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    private final UserService userService;
    private final UserRepository userRepository;
    private UserInfo userInfo;

    public UserServiceTest() {
        this.userRepository = new FakeUserRepository();
        this.userService = new UserService(this.userRepository);
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
        User newUser = userService.createUser(dto);

        //then
        assertEquals(1L , newUser.getId());
    }

    @Test
    void givenCreateUserRequestDto_whenCreateUser_thanCanFindUser() {
        // given
        CreateUserRequestDto dto = new CreateUserRequestDto(userInfo.getName() , userInfo.getProfileImageUrl());

        //when
        User newUser = userService.createUser(dto);
        User foundUser = userService.getUser(newUser.getId());

        //then
        assertEquals(newUser.getId(), foundUser.getId());
    }

}
