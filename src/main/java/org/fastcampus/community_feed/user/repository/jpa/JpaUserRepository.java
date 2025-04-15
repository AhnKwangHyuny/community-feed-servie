package org.fastcampus.community_feed.user.repository.jpa;

import org.fastcampus.community_feed.user.domain.User;
import org.fastcampus.community_feed.user.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    @Modifying
    @Query(value = "UPDATE UserEntity u "
            + "SET u.followerCount=:#{#user.followerCount()}, u.followingCount=:#{#user.followingCount()} "
            + "WHERE u.id=:#{#user.id}")
    void updateUserFollowerFollowingCount(User user);
}