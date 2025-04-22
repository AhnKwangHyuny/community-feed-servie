package org.faddy.community_feed.acceptance.auth.email;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.faddy.community_feed.acceptance.auth.SignUpAcceptanceSteps;
import org.faddy.community_feed.acceptance.utils.AcceptanceTestTemplate;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.common.config.TestRedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@Import(TestRedisConfig.class)
@ActiveProfiles("test")
public class EmailVerificationAcceptanceTest extends AcceptanceTestTemplate {

    private final String email = "test@naver.com";

    @MockBean(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    public void setup() {
        this.cleanUp();
    }

    @Test
    public void givenEmailRequest_whenSendVerification_thenTokenStoredInRedis() {
//        // given
//        SendEmailRequestDto dto = new SendEmailRequestDto(email);
//
//        // when
//        Integer code = SignUpAcceptanceSteps.requestSendEmail(dto);
//
//        // then
//        assertEquals(0, code);
    }

    @Test
    public void givenStoredToken_whenVerifyWithCorrectToken_thenVerificationSucceeds() {
//        // given
//        SendEmailRequestDto dto = new SendEmailRequestDto(email);
//        SignUpAcceptanceSteps.requestSendEmail(dto);
//        String token = getEmailToken(email);
//
//        // when
//        Integer code = SignUpAcceptanceSteps.requestVerifyEmail(email, token);
//
//        // then
//        assertEquals(0, code);
//        assertTrue(isEmailVerified(email));
    }

    @Test
    public void givenStoredToken_whenVerifyWithIncorrectToken_thenVerificationFails() {
        // given
//        SendEmailRequestDto dto = new SendEmailRequestDto(email);
//        SignUpAcceptanceSteps.requestSendEmail(dto);
//
//        // when
//        Integer code = SignUpAcceptanceSteps.requestVerifyEmail(email, "invalid-token");
//
//        // then
//        assertEquals(500, code);
//        assertFalse(isEmailVerified(email));
    }
}