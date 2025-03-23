package org.faddy.common.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.junit.jupiter.api.Test;

public class CreateUserTest {

    @Test
    void givenNameAndProfileImageUrl_whenCreated_thenThrowNothing() {
        //given
        String name = "abcd";
        String profileImageUrl = "";

        //when
        //then
        assertDoesNotThrow(() -> new UserInfo(name , profileImageUrl));
    }

    @Test
    void givenBlankNameAndProfileImage_whenCreated_thenThrowError() {
        //given
        String name = "";
        String profileImageUrl = "";

        //when
        //then
        assertThrows(IllegalArgumentException.class , () -> new UserInfo(name, profileImageUrl));

    }

}
