package org.faddy.community_feed.auth.application.interfaces;

import java.time.Duration;

/**
 * 이메일 인증 토큰 캐싱
 */
public interface EmailVerificationCacheRepository {

    /**
     * 이메일과 인증 토큰을 캐싱
     * @param email 이메일 주소
     * @param token 인증 토큰
     * @param expiry 만료 시간
     */
    void saveEmailVerificationToken(String email, String token, Duration expiry);

    /**
     * 저장된 인증 토큰을 조회
     * @param email 이메일 주소
     * @return 인증 토큰, 없으면 null
     */
    String getEmailVerificationToken(String email);

    /**
     * 인증 토큰을 검증
     * @param email 이메일 주소
     * @param token 검증할 토큰
     * @return 토큰이 일치하면 true, 그렇지 않으면 false
     */
    boolean verifyEmailToken(String email, String token);

    /**
     * 인증 토큰을 삭제
     * @param email 이메일 주소
     */
    void removeEmailVerificationToken(String email);
}