package org.fastcampus.community_feed.acceptance.utils;

import static org.fastcampus.community_feed.acceptance.post.UserAcceptanceSteps.createUser;
import static org.fastcampus.community_feed.acceptance.post.UserAcceptanceSteps.followUser;

import static org.fastcampus.community_feed.acceptance.auth.LoginAcceptanceSteps.requestLoginGetToken;
import static org.fastcampus.community_feed.acceptance.auth.SignUpAcceptanceSteps.registerUser;
import static org.fastcampus.community_feed.acceptance.auth.SignUpAcceptanceSteps.requestSendEmail;
import static org.fastcampus.community_feed.acceptance.auth.SignUpAcceptanceSteps.requestVerifyEmail;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.fastcampus.community_feed.auth.application.dto.CreateUserAuthRequestDto;
import org.fastcampus.community_feed.auth.application.dto.LoginRequestDto;
import org.fastcampus.community_feed.auth.application.dto.SendEmailRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void loadData() {
        // user 1, 2, 3 생성
        for (int i = 1; i < 4; i++) {
            createUser("user" + i + "@test.com");
        }
    }

    public String getEmailToken(String email) {
        return entityManager.createNativeQuery("SELECT token FROM community_email_verification WHERE email = ?", String.class)
                .setParameter(1, email)
                .getSingleResult()
                .toString();
    }

    public boolean isEmailVerified(String email) {
        return entityManager.createQuery("SELECT isVerified FROM EmailVerificationEntity WHERE email = :email", Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public Long getUserId(String email) {
        return entityManager.createQuery("SELECT userId FROM UserAuthEntity WHERE email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public void createUser(String email) {
        String password = "password";
        requestSendEmail(new SendEmailRequestDto(email));
        String token = getEmailToken(email);
        requestVerifyEmail(email, token);
        registerUser(new CreateUserAuthRequestDto(email, password, "USER", "nickname", "profile"));
    }
}