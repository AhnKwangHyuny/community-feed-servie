package org.faddy.community_feed.common.idempotency.repository;

import org.faddy.community_feed.common.idempotency.Idempotency;

public interface IdempotencyRepository {
    Idempotency getByKey(String key);
    void save(Idempotency idempotency);
}