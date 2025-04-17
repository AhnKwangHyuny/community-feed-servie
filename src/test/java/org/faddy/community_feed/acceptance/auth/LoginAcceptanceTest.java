package org.faddy.community_feed.acceptance.auth;


import static org.faddy.community_feed.acceptance.auth.LoginAcceptanceSteps.requestLoginGetCode;
import static org.faddy.community_feed.acceptance.auth.LoginAcceptanceSteps.requestLoginGetToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.faddy.community_feed.acceptance.utils.AcceptanceTestTemplate;
import org.faddy.community_feed.auth.application.dto.LoginRequestDto;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.auth.domain.TokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginAcceptanceTest extends AcceptanceTestTemplate {

    private final String email = "email@naver.com";
    private final TokenProvider tokenProvider = new TokenProvider("testteststestteststestteststestteststestteststestteststestteststestteststestteststesttests");


    /*
    * 1. 이메일을 보내고
    * 2. 이메일을 확인하고
    * 3. 사용자를 등록한다.
    * */
    @BeforeEach
    void init() {
        super.cleanUp();
        this.createUser(email);
    }

    @Test
    void givenEmailAndPassword_whenLogin_thenToken() {
        //given

        //when
        Integer code = requestLoginGetCode(new LoginRequestDto(email, "password"));

        //then
        Assertions.assertEquals(code, 0);
    }

    @Test
    void givenWrongPassword_whenLogin_thenException() {
        // given
        LoginRequestDto dto = new LoginRequestDto(email, "wrongPassword");

        // when
        Integer code = requestLoginGetCode(dto);

        // then
        assertEquals(400, code);
    }
}
