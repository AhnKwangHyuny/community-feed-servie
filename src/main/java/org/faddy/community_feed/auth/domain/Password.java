package org.faddy.community_feed.auth.domain;

import lombok.Getter;

@Getter
public class Password {

    private final String encryptedPassword;

    private Password(String password) {
        if(password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        this.encryptedPassword = password;
    }

    public static Password createEncryptedPassword(String password) {

        String encrypted = SHA256.encrypt(password);

        return new Password(encrypted);
    }

    public static Password createPassword(String encryptedPassword) {

        return new Password(encryptedPassword);
    }

    public boolean matchPassword(String password) {

        return this.encryptedPassword.equals(SHA256.encrypt(password));
    }
}
