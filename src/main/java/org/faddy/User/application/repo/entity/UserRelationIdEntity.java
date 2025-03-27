package org.faddy.User.application.repo.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.faddy.common.repo.entity.TimeBaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserRelationIdEntity{

    private Long followerUserId;
    private Long followingUserId;
}
