package org.faddy.community_feed.infra.s3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.faddy.community_feed.infra.s3.application.S3Service;
import org.faddy.community_feed.infra.s3.application.dto.S3ObjectDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class S3ServiceTest {

    @Mock
    private AmazonS3 amazonS3;
    
    @InjectMocks
    private S3Service s3Service;

    private static final String BUCKET_NAME = "community-feed-service";

    @Test
    @DisplayName("S3에 파일 업로드 시 성공적으로 업로드되어야 함")
    void givenImageFileWhenUploadThenShouldReturnS3ObjectDto() throws IOException {
        // given

        ClassPathResource resource = new ClassPathResource("images/testImages/test_default_image.png");
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
        
        MultipartFile mockFile = new MockMultipartFile(
            "test_default_image.png", 
            "test_default_image.png", 
            "image/png", 
            imageBytes
        );
        
        // S3 모킹
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL("https://test-bucket.s3.amazonaws.com/test-key"));
        
        // when
        S3ObjectDto result = s3Service.uploadFile(mockFile, "test-uploads");
        
        // then
        assertNotNull(result);
        assertNotNull(result.getUrl());
        assertNotNull(result.getKey());
        assertEquals("test_default_image.png", result.getOriginalFilename());
        assertEquals("image/png", result.getContentType());
        
        // S3 putObject가 호출되었는지 확인
        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));
        // S3 getUrl이 호출되었는지 확인
        verify(amazonS3, times(1)).getUrl(eq(BUCKET_NAME), anyString());
    }
    
    @Test
    @DisplayName("S3에 업로드된 파일을 삭제할 수 있어야 함")
    void givenUploadedFileWhenDeleteThenFileShouldBeDeleted() {
        // given

        String testKey = "test-key";
        
        // S3 모킹
        doNothing().when(amazonS3).deleteObject(anyString(), anyString());
        
        // when
        s3Service.deleteFile(testKey);
        
        // then
        // S3 deleteObject가 호출되었는지 확인
        verify(amazonS3, times(1)).deleteObject(eq(BUCKET_NAME), eq(testKey));
    }
    
    @Test
    @DisplayName("바이트 배열로 이미지를 업로드할 수 있어야 함")
    void givenImageBytesWhenUploadThenShouldReturnS3ObjectDto() throws IOException {
        // given
        
        ClassPathResource resource = new ClassPathResource("images/testImages/test_default_image.png");
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
        
        // S3 모킹
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL("https://test-bucket.s3.amazonaws.com/test-key"));
        
        // when
        S3ObjectDto result = s3Service.uploadFile(
            imageBytes, 
            "image/png", 
            "test_bytes_upload.png", 
            "test-bytes"
        );
        
        // then
        assertNotNull(result);
        assertNotNull(result.getUrl());
        assertNotNull(result.getKey());
        assertEquals("test_bytes_upload.png", result.getOriginalFilename());
        
        // S3 putObject가 호출되었는지 확인
        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));
    }
}
