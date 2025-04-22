package org.faddy.community_feed.auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationCacheRepository;
import org.faddy.community_feed.common.cache.CacheKeys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class EmailVerificationCacheRepositoryImpl implements EmailVerificationCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveEmailVerificationToken(String email, String token, Duration expiry) {
        String key = CacheKeys.emailVerificationTokenKey(email);
        redisTemplate.opsForValue().set(key, token, expiry);
    }

    @Override
    public String getEmailVerificationToken(String email) {
        String key = CacheKeys.emailVerificationTokenKey(email);
        System.out.println("key = " + key);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean verifyEmailToken(String email, String token) {
        String savedToken = getEmailVerificationToken(email);
        return savedToken != null && savedToken.equals(token);
    }

    @Override
    public void removeEmailVerificationToken(String email) {
        String key = CacheKeys.emailVerificationTokenKey(email);
        redisTemplate.delete(key);
    }
}
