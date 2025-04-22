package org.faddy.community_feed.auth.repository.jpa;

import java.util.Optional;
import org.faddy.community_feed.auth.repository.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {
    Optional<EmailVerificationEntity> findByEmail(String email);

    Optional<EmailVerificationEntity> findByEmailAndToken(String email, String token);
}
