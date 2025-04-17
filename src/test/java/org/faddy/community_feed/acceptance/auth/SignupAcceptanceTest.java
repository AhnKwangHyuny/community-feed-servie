package org.faddy.community_feed.acceptance.auth;


import org.faddy.community_feed.acceptance.utils.AcceptanceTestTemplate;
import org.faddy.community_feed.auth.application.dto.CreateUserAuthRequestDto;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SignupAcceptanceTest extends AcceptanceTestTemplate {

    private final String email = "test@naver.com";

    @BeforeEach
    public void setup() {
        this.cleanUp();
    }

    @Test
    public void givenSendEmail_whenVerifyEmail_thenVerifiedEmail() {
        //given
        SendEmailRequestDto dto = new SendEmailRequestDto(email);
        SignUpAcceptanceSteps.requestSendEmail(dto);

        //when
        String emailToken = getEmailToken(email);
        Integer code = SignUpAcceptanceSteps.requestVerifyEmail(email , emailToken);

        //then
        boolean emailVerified = isEmailVerified(email);
        Assertions.assertEquals(0 , code);
        Assertions.assertTrue(emailVerified);
    }

    @Test
    public void givenSendEmail_whenVerifyEmailWithWrongToken_thenEmailNotVerified() {
        //given
        SendEmailRequestDto dto = new SendEmailRequestDto(email);

        //when
        SignUpAcceptanceSteps.requestSendEmail(dto);

        //then
        Integer code = SignUpAcceptanceSteps.requestVerifyEmail(email, "wrong token");
        Assertions.assertEquals(500 , code);
    }

    @Test
    public void givenVerifyEmail_whenDuplicatedVerifiedEmail_thenThrowError() {
        //given
        SendEmailRequestDto dto = new SendEmailRequestDto(email);
        SignUpAcceptanceSteps.requestSendEmail(dto);

        String token = getEmailToken(email);
        SignUpAcceptanceSteps.requestVerifyEmail(email, token);

        //when
        Integer code = SignUpAcceptanceSteps.requestVerifyEmail(email, token);
        //then
        Assertions.assertEquals(500 , code);
    }

    @Test
    public void givenSendEmail_whenVerifyEmailWithWrongEmail_thenThrowError() {
        //given
        SignUpAcceptanceSteps.requestSendEmail(new SendEmailRequestDto(email));
        String token = getEmailToken(email);
        //when
        Integer code = SignUpAcceptanceSteps.requestVerifyEmail("agh0315@naver.com", token);

        //then
        Assertions.assertEquals(500 , code);
    }

    @Test
    public void givenVerifiedEmail_whenRegisterUser_thenUserRegistered() {
        //given
        SignUpAcceptanceSteps.requestSendEmail(new SendEmailRequestDto(email));
        String token = getEmailToken(email);
        SignUpAcceptanceSteps.requestVerifyEmail(email, token);

        //when
        CreateUserAuthRequestDto dto = new CreateUserAuthRequestDto(email, "password" , "USER", "안광현", "profile_image");

        //then
        Integer code = SignUpAcceptanceSteps.registerUser(dto);
        Assertions.assertEquals(0 , code);
    }
}
