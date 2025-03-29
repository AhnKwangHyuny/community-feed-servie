package org.faddy.User.infrastructure.repo.jpa;

import java.util.List;
import org.faddy.User.application.dto.GetUserListResponseDto;
import org.faddy.User.infrastructure.repo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserListQueryRepository extends JpaRepository<UserEntity , Long> {

    @Query(value = "SELECT new org.faddy.User.application.dto.GetUserListResponseDto(u.name , u.profileImageUrl)"
        + " FROM UserRelationEntity ur "
        + " INNER JOIN UserEntity u ON ur.followingId = u.id "
        + "WHERE ur.followerId = :userId")
    List<GetUserListResponseDto> getFollowersList(Long userId);

    @Query(value = "SELECT new org.faddy.User.application.dto.GetUserListResponseDto(u.name , u.profileImageUrl)"
        + " FROM UserRelationEntity ur "
        + " INNER JOIN UserEntity u ON ur.followerId = u.id "
        + "WHERE ur.followingId = :userId")
    List<GetUserListResponseDto> getFollowingList(Long userId);
}