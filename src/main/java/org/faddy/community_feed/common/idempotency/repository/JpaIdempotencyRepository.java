package org.faddy.community_feed.common.idempotency.repository;

import java.util.Optional;
import org.faddy.community_feed.common.idempotency.entity.IdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIdempotencyRepository extends JpaRepository<IdempotencyEntity, Long> {
    Optional<IdempotencyEntity> findByIdempotencyKey(String key);
}
