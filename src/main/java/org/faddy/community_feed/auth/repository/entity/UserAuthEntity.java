package org.faddy.community_feed.auth.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.auth.domain.UserAuth;
import org.springframework.data.annotation.CreatedDate;

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

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    public UserAuthEntity(UserAuth userAuth, Long userId) {
        this.email = userAuth.getEmail();
        this.password = userAuth.getPassword();
        this.role = userAuth.getRole();
        this.userId = userId;
    }

    public void updateLastLoginDate() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public UserAuth toUserAuth() {
        UserAuth auth =  new UserAuth(userId, email, password, role);
        auth.updateLastLoginDate(lastLoginAt);

        return auth;
    }
}
