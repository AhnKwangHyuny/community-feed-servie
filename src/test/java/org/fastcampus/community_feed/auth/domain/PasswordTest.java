package org.fastcampus.community_feed.auth.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    void givenPassword_whenMatchSamePassword_thenReturnTrue() {
        Password password = Password.createEncryptedPassword("password");

        assertTrue(password.matchPassword("password"));
    }

    @Test
    void givenPassword_whenMatchDiffPassword_thenReturnTrue() {
        Password password = Password.createEncryptedPassword("password1");

        assertFalse(password.matchPassword("password"));
    }

    @Test
    void givenPassword_whenMatchNullPassword_thenReturnFalse() {
        Password password = Password.createPassword(SHA256.encrypt("password"));

        assertTrue(password.matchPassword("password"));
    }
}
