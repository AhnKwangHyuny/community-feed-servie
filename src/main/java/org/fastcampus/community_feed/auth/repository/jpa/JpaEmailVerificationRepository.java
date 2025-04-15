package org.fastcampus.community_feed.auth.repository.jpa;

import java.util.Optional;
import org.fastcampus.community_feed.auth.repository.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {
    Optional<EmailVerificationEntity> findByEmail(String email);
}
