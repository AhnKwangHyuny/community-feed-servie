package org.faddy.community_feed.auth.infrastructure.email;

import org.faddy.community_feed.auth.domain.Email;

public interface EmailSender {
    /**
        * 인증 이메일 전송
     * @param email 수신자 이메일
     * @param verificationToken 인증 토큰
     * @return 전송 성공 여부
     */
    boolean sendVerificationEmail(Email email, String verificationToken);
}
