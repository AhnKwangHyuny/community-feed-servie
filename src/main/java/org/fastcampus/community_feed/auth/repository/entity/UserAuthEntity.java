package org.fastcampus.community_feed.auth.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fastcampus.community_feed.auth.domain.UserAuth;

@Entity
@Table(name="community_user_auth")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserAuthEntity {

    @Id
    private String email;
    private String password;
    private String role;
    private Long userId;

    public UserAuthEntity(UserAuth userAuth, Long userId) {
        this.email = userAuth.getEmail();
        this.password = userAuth.getPassword();
        this.role = userAuth.getRole();
        this.userId = userId;
    }

    public UserAuth toUserAuth() {
        return new UserAuth(userId, email, password, role);
    }
}
