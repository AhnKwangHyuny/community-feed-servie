package org.fastcampus.community_feed.auth.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.auth.application.interfaces.EmailVerificationRepository;
import org.fastcampus.community_feed.auth.domain.Email;
import org.fastcampus.community_feed.auth.repository.entity.EmailVerificationEntity;
import org.fastcampus.community_feed.auth.repository.jpa.JpaEmailVerificationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final JpaEmailVerificationRepository jpaEmailVerificationRepository;

    @Override
    @Transactional
    public void createEmailVerification(Email email, String randomToken) {
        String emailAddress = email.getEmailText();
        Optional<EmailVerificationEntity> entity = jpaEmailVerificationRepository.findByEmail(emailAddress);

        if (entity.isPresent()) {
            EmailVerificationEntity emailVerificationEntity = entity.get();
            if (emailVerificationEntity.isVerified()) {
                throw new IllegalArgumentException("이미 인증된 이메일입니다.");
            }

            emailVerificationEntity.updateToken(randomToken);
            return;
        }

        EmailVerificationEntity emailVerificationEntity = new EmailVerificationEntity(emailAddress, randomToken);
        jpaEmailVerificationRepository.save(emailVerificationEntity);
    }

    @Override
    @Transactional
    public void verifyEmail(Email email, String token) {
        String emailAddress = email.getEmailText();
        EmailVerificationEntity entity = jpaEmailVerificationRepository.findByEmail(emailAddress)
                .orElseThrow(() -> new IllegalArgumentException("인증되지 않은 이메일입니다."));

        if (entity.isVerified()) {
            throw new IllegalArgumentException("이미 인증된 이메일입니다.");
        }

        if (!entity.hasSameToken(token)) {
            throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
        }

        entity.verify();
    }

    @Override
    public boolean isEmailVerified(Email email) {
        EmailVerificationEntity entity = jpaEmailVerificationRepository.findByEmail(email.getEmailText())
                .orElseThrow(() -> new IllegalArgumentException("인증되지 않은 이메일입니다."));

        return entity.isVerified();
    }
}
