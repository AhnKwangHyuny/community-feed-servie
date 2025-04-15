package org.fastcampus.community_feed.auth.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RandomTokenGeneratorTest {

    @Test
    void whenGenerateToken_thenReturnTokenWithCorrectLength() {
        // when
        String token = RandomTokenGenerator.generateToken();

        // then
        assertNotNull(token);
        assertEquals(16, token.length());
    }

    @Test
    void whenGenerateToken_thenReturnTokenWithValidCharacters() {
        // when
        String token = RandomTokenGenerator.generateToken();

        // then
        assertNotNull(token);
        assertTrue(token.matches("[0-9A-Za-z]{16}"));
    }

    @Test
    void whenGenerateTokenMultipleTimes_thenReturnUniqueTokens() {
        // when
        String token1 = RandomTokenGenerator.generateToken();
        String token2 = RandomTokenGenerator.generateToken();

        // then
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }
}
