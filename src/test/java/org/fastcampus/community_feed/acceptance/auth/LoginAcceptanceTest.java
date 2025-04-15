package org.fastcampus.community_feed.acceptance.auth;


import static org.fastcampus.community_feed.acceptance.auth.LoginAcceptanceSteps.requestLoginGetCode;
import static org.fastcampus.community_feed.acceptance.auth.LoginAcceptanceSteps.requestLoginGetToken;
import static org.fastcampus.community_feed.acceptance.auth.SignUpAcceptanceSteps.registerUser;
import static org.fastcampus.community_feed.acceptance.auth.SignUpAcceptanceSteps.requestSendEmail;
import static org.fastcampus.community_feed.acceptance.auth.SignUpAcceptanceSteps.requestVerifyEmail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.fastcampus.community_feed.acceptance.utils.AcceptanceTestTemplate;
import org.fastcampus.community_feed.auth.application.dto.CreateUserAuthRequestDto;
import org.fastcampus.community_feed.auth.application.dto.LoginRequestDto;
import org.fastcampus.community_feed.auth.application.dto.SendEmailRequestDto;
import org.fastcampus.community_feed.auth.domain.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginAcceptanceTest extends AcceptanceTestTemplate {

    private final String email = "email@email.com";
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
        // given
        LoginRequestDto dto = new LoginRequestDto(email, "password");

        // when
        String token = requestLoginGetToken(dto);

        // then
        assertNotNull(token);
        Long id = tokenProvider.getUserId(token);
        assertEquals(1L, id);
    }

    @Test
    void givenWrongPassword_whenLogin_thenException() {
        // given
        LoginRequestDto dto = new LoginRequestDto(email, "wrongPassword");

        // when
        Integer code = requestLoginGetCode(dto);

        // then
        assertEquals(500, code);
    }
}
