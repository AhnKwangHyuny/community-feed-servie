package org.fastcampus.community_feed.acceptance.auth;

import io.restassured.RestAssured;
import org.fastcampus.community_feed.auth.application.dto.LoginRequestDto;
import org.fastcampus.community_feed.auth.application.dto.UserAccessTokenResponseDto;

public class LoginAcceptanceSteps {
    public static String requestLoginGetToken(LoginRequestDto dto) {
        UserAccessTokenResponseDto res = RestAssured.given()
                .body(dto)
                .contentType("application/json")
                .when()
                .post("/login")
                .then()
                .extract()
                .jsonPath()
                .getObject("value", UserAccessTokenResponseDto.class);
        return res.accessToken();
    }

    public static Integer requestLoginGetCode(LoginRequestDto dto) {
        return RestAssured.given()
                .body(dto)
                .contentType("application/json")
                .when()
                .post("/login")
                .then()
                .extract()
                .jsonPath()
                .getObject("code", Integer.class);
    }
}
