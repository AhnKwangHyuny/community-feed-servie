package org.fastcampus.community_feed.auth.repository.jpa;

import java.util.Optional;
import org.fastcampus.community_feed.auth.repository.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserAuthRepository extends JpaRepository<UserAuthEntity, Long> {

    Optional<UserAuthEntity> findByEmail(String email);
}
