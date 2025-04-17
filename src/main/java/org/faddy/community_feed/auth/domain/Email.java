package org.faddy.community_feed.auth.domain;

import java.util.regex.Pattern;
import lombok.Getter;

/**
 * 이메일을 표현하는 값 객체
 */
@Getter
public class Email {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private final String emailText;
    private final String domain;

    /**
     * 이메일 생성 팩토리 메서드
     */
    public static Email createEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        if (!validateEmailFormat(email)) {
            throw new IllegalArgumentException("Email format is not valid");
        }

        return new Email(email);
    }

    private static boolean validateEmailFormat(String email) {
        return pattern.matcher(email).matches();
    }

    private static String extractDomain(String email) {
        return email.substring(email.lastIndexOf('@') + 1).toLowerCase();
    }

    private Email(String email) {
        this.emailText = email;
        this.domain = extractDomain(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return emailText.equals(email.emailText);
    }

    @Override
    public int hashCode() {
        return emailText.hashCode();
    }

    @Override
    public String toString() {
        return emailText;
    }
}
