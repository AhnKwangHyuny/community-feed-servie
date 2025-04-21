package org.faddy.community_feed.message.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.auth.domain.UserAuth;

@Entity
@Table(name = "community_fcm_token")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FcmTokenEntity {

    @Id
    private Long userId;

    private String token;

    public static FcmTokenEntity createToken(Long userId, String token) {

        return new FcmTokenEntity(userId, token);
    }
}
