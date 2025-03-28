package org.faddy.User.application.repo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.common.infrastructure.entity.TimeBaseEntity;

@Entity
@Table(name = "community_user_relations")
@IdClass(UserRelationIdEntity.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRelationEntity extends TimeBaseEntity {

    @Id
    @Column(name = "follower_id")
    private Long followerId;

    @Id
    @Column(name = "following_id")
    private Long followingId;
}