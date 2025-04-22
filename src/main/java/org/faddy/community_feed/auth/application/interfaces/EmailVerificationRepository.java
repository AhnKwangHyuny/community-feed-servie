package org.faddy.community_feed.auth.application.interfaces;

import org.faddy.community_feed.auth.domain.Email;
import org.faddy.community_feed.auth.repository.entity.EmailVerificationEntity;

public interface EmailVerificationRepository {
    void createEmailVerification(Email email, String token);

    void verifyEmail(Email email, String token);

    boolean isEmailVerified(Email email);

    EmailVerificationEntity getEmailVerification(String email, String token);
}
