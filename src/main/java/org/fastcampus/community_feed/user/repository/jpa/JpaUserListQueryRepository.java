package org.fastcampus.community_feed.user.repository.jpa;

import java.util.List;
import org.fastcampus.community_feed.user.application.dto.GetUserListResponseDto;
import org.fastcampus.community_feed.user.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserListQueryRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT new org.fastcampus.community_feed.user.application.dto.GetUserListResponseDto(u.name, u.profileImage) "
            + "FROM UserRelationEntity ur "
            + "INNER JOIN UserEntity u ON ur.followerUserId = u.id "
            + "WHERE ur.followingUserId = :userId")
    List<GetUserListResponseDto> getFollowingList(Long userId);

    @Query(value = "SELECT new org.fastcampus.community_feed.user.application.dto.GetUserListResponseDto(u.name, u.profileImage) "
            + "FROM UserRelationEntity ur "
            + "INNER JOIN UserEntity u ON ur.followingUserId = u.id "
            + "WHERE ur.followerUserId = :userId")
    List<GetUserListResponseDto> getFollowerList(Long userId);
}
