package org.faddy.community_feed.auth.application.dto;

import lombok.Builder;
import lombok.Getter;
import org.faddy.community_feed.auth.domain.UserAuth;

@Builder
public record SendEmailVerificationTokenResponseDto(String email, String verificationToken , boolean sent) {

    public static SendEmailVerificationTokenResponseDto of(String email, String verificationToken, boolean sent) {
        return new SendEmailVerificationTokenResponseDto(email, verificationToken, sent);
    }

}
