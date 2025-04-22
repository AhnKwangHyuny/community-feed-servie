package org.faddy.community_feed.auth.application.dto;

public record VerifyEmailResponseDto(
    String email,
    boolean verified,
    String message
) {
    public static VerifyEmailResponseDto of(String email, boolean verified, String message) {
        return new VerifyEmailResponseDto(email, verified, message);
    }
}