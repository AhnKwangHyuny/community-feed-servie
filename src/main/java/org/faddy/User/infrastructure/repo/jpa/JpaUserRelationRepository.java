package org.faddy.User.infrastructure.repo.jpa;

import java.util.List;
import org.faddy.User.infrastructure.repo.entity.UserRelationEntity;
import org.faddy.User.infrastructure.repo.entity.UserRelationIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRelationRepository extends
    JpaRepository<UserRelationEntity, UserRelationIdEntity> {

    @Query("SELECT u.followerId FROM UserRelationEntity u WHERE u.followerId = :userId")
    List<Long> findFollowers(Long userId);
}
