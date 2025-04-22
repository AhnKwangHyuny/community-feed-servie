package org.faddy.community_feed.common.cache;


public class CacheKeys {

    private static final String PREFIX = "faddy:";

    /**
     * 이메일 인증 토큰 키
     */
    public static final String EMAIL_VERIFICATION_TOKEN_PREFIX = PREFIX + "email:verification:";

    /**
     * 이메일 인증 토큰 키 생성
     * @param email 이메일 주소
     * @return 캐시 키
     */
    public static String emailVerificationTokenKey(String email) {
        return EMAIL_VERIFICATION_TOKEN_PREFIX + email;
    }

    private CacheKeys() {
        // 인스턴스화 방지
    }
}