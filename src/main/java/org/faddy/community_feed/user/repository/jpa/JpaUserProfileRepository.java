package org.faddy.community_feed.user.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.faddy.community_feed.user.repository.entity.UserProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUserProfileRepository extends JpaRepository<UserProfileImageEntity, Long> {

    @Query("SELECT p FROM UserProfileImageEntity p JOIN FETCH p.image " +
        "WHERE p.user.id = :userId AND p.isActive = true " +
        "ORDER BY p.regDt DESC")
    Optional<UserProfileImageEntity> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM UserProfileImageEntity p JOIN FETCH p.image " +
        "WHERE p.user.id = :userId " +
        "ORDER BY p.regDt DESC")
    List<UserProfileImageEntity> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserProfileImageEntity p SET p.isActive = false WHERE p.user.id = :userId")
    void deactivateAllByUserId(@Param("userId") Long userId);
}