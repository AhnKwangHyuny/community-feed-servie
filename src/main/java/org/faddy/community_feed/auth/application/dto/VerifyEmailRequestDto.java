package org.faddy.community_feed.auth.application.dto;

import lombok.Data;

@Data
public class VerifyEmailRequestDto {

    private String email;

    private String token;

}
