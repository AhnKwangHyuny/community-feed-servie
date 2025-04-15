package org.fastcampus.community_feed.auth.application.interfaces;

import org.fastcampus.community_feed.auth.domain.Email;

public interface EmailSendRepository {
    void sendVerificationEmail(Email email, String token);
}
