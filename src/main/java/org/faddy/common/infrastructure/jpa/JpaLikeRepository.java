package org.faddy.common.infrastructure.jpa;

import org.faddy.common.infrastructure.entity.like.LikeEntity;
import org.faddy.common.infrastructure.entity.like.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLikeRepository extends JpaRepository<LikeEntity, LikeId> {

}
