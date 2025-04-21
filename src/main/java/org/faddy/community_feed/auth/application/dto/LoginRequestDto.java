package org.faddy.community_feed.auth.application.dto;

public record LoginRequestDto(String email, String password , String fcmToken) {

}
