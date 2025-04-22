package org.faddy.community_feed.common.utils;

import java.security.SecureRandom;

public class EmailVerificationTokenProvider {
    private static final SecureRandom random = new SecureRandom();
    private static final int TOKEN_LENGTH = 6;

    /**
     * 6자리 숫자 토큰을 생성 (000000 ~ 999999).
     */
    public static String generate6DigitToken() {
        int token = random.nextInt(1_000_000);
        return String.format("%06d", token);
    }
}
