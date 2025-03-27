package org.faddy.User.application.repo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.faddy.common.domain.PositiveInteger;
import org.faddy.common.repo.entity.TimeBaseEntity;

@Entity
@Table(name = "community_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "following_count", nullable = false)
    private int followingCount;

    @Column(name = "follower_count", nullable = false)
    private int followerCount;

    // Entity -> Domain 변환 메소드
    public User toUser() {
        return User.builder()
            .id(this.id)
            .info(new UserInfo(this.name, this.profileImageUrl))
            .followerCounter(new PositiveInteger(this.followerCount))
            .followingCounter(new PositiveInteger(this.followingCount))
            .build();
    }

    // Domain -> Entity 변환 (생성)
    public static UserEntity fromUser(User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.getId();
        entity.name = user.getInfo().getName();
        entity.profileImageUrl = user.getInfo().getProfileImageUrl();
        entity.followingCount = user.getFollowingCount();
        entity.followerCount = user.getFollowerCount();

        return entity;
    }

    // 엔티티 업데이트 메소드
    public void update(User user) {
        this.name = user.getInfo().getName();
        this.profileImageUrl = user.getInfo().getProfileImageUrl();
        this.followingCount = user.getFollowingCount();
        this.followerCount = user.getFollowerCount();
    }
}