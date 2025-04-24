package org.faddy.community_feed.acceptance.image;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ImageUploadAcceptanceSteps {

    private static final String BASE_URL = "http://localhost:8080";

    /**
     * 이미지 업로드 API 호출
     * 
     * @param token 인증 토큰
     * @param imageType 이미지 타입 (POST, PROFILE, OTHER)
     * @return 응답 객체
     */
    public static Response requestImageUpload(String token, String imageType) throws IOException {
        // 테스트 이미지 파일 준비
        ClassPathResource resource = new ClassPathResource("images/testImages/test_default_image.png");
        File imageFile = resource.getFile();
        
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("file", imageFile, "image/png")
                .formParam("type", imageType)
                .when()
                .post("/api/images");
    }
    
    /**
     * 이미지 업로드 후 ID 반환
     * 
     * @param token 인증 토큰
     * @param imageType 이미지 타입
     * @return 업로드된 이미지 ID
     */
    public static Long requestImageUploadGetId(String token, String imageType) throws IOException {
        Response response = requestImageUpload(token, imageType);
        
        if (response.statusCode() != HttpStatus.OK.value()) {
            throw new RuntimeException("Failed to upload image: " + response.body().asString());
        }
        
        return response.jsonPath().getLong("data.id");
    }
    
    /**
     * 이미지가 포함된 게시물 생성 API 호출
     * 
     * @param token 인증 토큰
     * @param content 게시물 내용
     * @param state 게시물 상태
     * @return 응답 객체
     */
    public static Response requestCreatePostWithImage(String token, String content, String state) throws IOException {
        // 테스트 이미지 파일 준비
        ClassPathResource resource = new ClassPathResource("images/testImages/test_default_image.png");
        File imageFile = resource.getFile();
        
        // 게시물 데이터 JSON
        Map<String, Object> postData = new HashMap<>();
        postData.put("content", content);
        postData.put("state", state);
        
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("data", postData, MediaType.APPLICATION_JSON_VALUE)
                .multiPart("images", imageFile, "image/png")
                .when()
                .post("/post");
    }
    
    /**
     * 이미지 삭제 API 호출
     * 
     * @param token 인증 토큰
     * @param imageId 삭제할 이미지 ID
     * @return 응답 객체
     */
    public static Response requestDeleteImage(String token, Long imageId) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/images/" + imageId);
    }
}
