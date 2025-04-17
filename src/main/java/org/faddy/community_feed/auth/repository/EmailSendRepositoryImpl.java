package org.faddy.community_feed.auth.repository;

import jakarta.transaction.Transactional;
import org.faddy.community_feed.auth.application.interfaces.EmailSendRepository;
import org.faddy.community_feed.auth.domain.Email;
import org.springframework.stereotype.Repository;

@Repository
public class EmailSendRepositoryImpl implements EmailSendRepository {


    @Override
    @Transactional
    public void sendVerificationEmail(Email email, String token) {
        // send email
    }
}




