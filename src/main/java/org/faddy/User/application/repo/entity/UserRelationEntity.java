package org.faddy.User.application.repo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.common.repo.entity.TimeBaseEntity;

@Entity
@Table(name = "community_user_relations")
@IdClass(UserRelationIdEntity.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRelationEntity extends TimeBaseEntity {

    @Id
    @Column(name = "follower_id")
    private Long followerUserId;

    @Id
    @Column(name = "following_id")
    private Long followingUserId;
}