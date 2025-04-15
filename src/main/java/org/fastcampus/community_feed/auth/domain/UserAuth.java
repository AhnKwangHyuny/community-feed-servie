package org.fastcampus.community_feed.auth.domain;

import lombok.Getter;

@Getter
public class UserAuth {

    private final Email email;
    private final Password password;
    private final UserRole role;
    private Long userId;

    public UserAuth(String email, String password, String role) {
        if (email == null || password == null || role == null) {
            throw new IllegalArgumentException("invalid auth information");
        }
        this.email = Email.createEmail(email);
        this.password = Password.createEncryptedPassword(password);
        this.role = UserRole.valueOf(role);
    }

    public UserAuth(Long userId, String email, String password, String role) {
        this.userId = userId;
        this.email = Email.createEmail(email);
        this.password = Password.createPassword(password);
        this.role = UserRole.valueOf(role);
    }

    public String getEmail() {
        return email.getEmailText();
    }

    public String getPassword() {
        return password.getEncryptedPassword();
    }

    public String getRole() {
        return role.name();
    }

    public boolean matchPassword(String password) {
        return this.password.matchPassword(password);
    }
}
