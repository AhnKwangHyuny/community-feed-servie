package org.fastcampus.community_feed.auth.domain;

import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class Email {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private final String emailText;

    public static Email createEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Email is not valid");
        }
        return new Email(email);
    }

    private static boolean validateEmail(String email) {
        return pattern.matcher(email).matches();
    }

    private Email(String email) {
        this.emailText = email;
    }
}
