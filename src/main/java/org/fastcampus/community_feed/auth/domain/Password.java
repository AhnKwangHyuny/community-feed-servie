package org.fastcampus.community_feed.auth.domain;

import lombok.Getter;

@Getter
public class Password {

    private final String encryptedPassword;

    private Password(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        this.encryptedPassword = password;
    }

    public static Password createEncryptedPassword(String password) {
        return new Password(SHA256.encrypt(password));
    }

    public static Password createPassword(String encryptedPassword) {
        return new Password(encryptedPassword);
    }

    public boolean matchPassword(String password) {
        return encryptedPassword.equals(SHA256.encrypt(password));
    }
}
