package org.faddy.community_feed.auth.respository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationCacheRepository;
import org.faddy.community_feed.auth.repository.EmailVerificationCacheRepositoryImpl;
import org.faddy.community_feed.common.cache.CacheKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class EmailVerificationCacheRepositoryImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private EmailVerificationCacheRepositoryImpl emailVerificationCacheRepository;

    private final String email = "test@naver.com";
    private final String token = "123456";
    private final String cacheKey = CacheKeys.emailVerificationTokenKey(email);

    @BeforeEach
    void setUp() {
        // lenient 모드로 설정하여 불필요한 스터빙 경고를 무시
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void whenSaveEmailVerificationToken_thenSavesWithCorrectExpiry() {
        // given
        Duration expiry = Duration.ofMinutes(3);

        // when
        emailVerificationCacheRepository.saveEmailVerificationToken(email, token, expiry);

        // then
        verify(valueOperations, times(1)).set(eq(cacheKey), eq(token), eq(expiry));
    }

    @Test
    void whenGetEmailVerificationToken_thenReturnsToken() {
        // given
        when(valueOperations.get(cacheKey)).thenReturn(token);

        // when
        String result = emailVerificationCacheRepository.getEmailVerificationToken(email);

        // then
        assertEquals(token, result);
        verify(valueOperations, times(1)).get(cacheKey);
    }

    @Test
    void whenGetEmailVerificationTokenForNonExistentKey_thenReturnsNull() {
        // given
        when(valueOperations.get(anyString())).thenReturn(null);

        // when
        String result = emailVerificationCacheRepository.getEmailVerificationToken(email);

        // then
        assertNull(result);
        verify(valueOperations, times(1)).get(cacheKey);
    }

    @Test
    void whenVerifyEmailTokenWithCorrectToken_thenReturnsTrue() {
        // given
        when(valueOperations.get(cacheKey)).thenReturn(token);

        // when
        boolean result = emailVerificationCacheRepository.verifyEmailToken(email, token);

        // then
        assertTrue(result);
        verify(valueOperations, times(1)).get(cacheKey);
    }

    @Test
    void whenVerifyEmailTokenWithIncorrectToken_thenReturnsFalse() {
        // given
        when(valueOperations.get(cacheKey)).thenReturn(token);

        // when
        boolean result = emailVerificationCacheRepository.verifyEmailToken(email, "wrong-token");

        // then
        assertFalse(result);
        verify(valueOperations, times(1)).get(cacheKey);
    }

    @Test
    void whenVerifyEmailTokenWithNonExistentKey_thenReturnsFalse() {
        // given
        when(valueOperations.get(cacheKey)).thenReturn(null);

        // when
        boolean result = emailVerificationCacheRepository.verifyEmailToken(email, token);

        // then
        assertFalse(result);
        verify(valueOperations, times(1)).get(cacheKey);
    }

    @Test
    void whenRemoveEmailVerificationToken_thenDeletesKey() {
        // when
        emailVerificationCacheRepository.removeEmailVerificationToken(email);

        // then
        verify(redisTemplate, times(1)).delete(cacheKey);
    }
}