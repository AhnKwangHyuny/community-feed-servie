package org.faddy.community_feed.acceptance.steps;

import io.restassured.RestAssured;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.springframework.http.MediaType;

public class SignUpAcceptanceSteps {

    public static Integer requestSendEmail(SendEmailRequestDto dto) {
        return RestAssured
            .given()
            .body(dto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/signup/send-verification-email")
            .jsonPath().get("code");
    }

}
