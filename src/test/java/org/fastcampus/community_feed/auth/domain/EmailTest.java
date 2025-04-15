package org.fastcampus.community_feed.auth.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @ParameterizedTest
    @NullAndEmptySource
    void givenEmailIsEmptyWhenCreateThenReturnEmail(String email) {
        // given email

        // when, then
        assertThrows(IllegalArgumentException.class, () -> Email.createEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"vaild/@ab", "naver.com", "natty#@naver", "안녕@하세요.com"})
    void givenEmailIsInvalidWhenCreateThenReturnEmail(String email) {
        // given

        // when, then
        assertThrows(IllegalArgumentException.class, () -> Email.createEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"vaild@ab", "a@naver.com", "natty@naver", "test@test.com"})
    void givenEmailValidWhenCreateThenReturnEmail(String email) {
        // given

        // when
        Email emailValue = Email.createEmail(email);

        // then
        assertEquals(email, emailValue.getEmailText());
    }
}
