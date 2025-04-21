package org.faddy.community_feed.message.repository.jpaRepository;

import org.faddy.community_feed.message.domain.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

}
