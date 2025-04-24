package org.faddy.community_feed.infra.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.infra.s3.application.dto.S3ObjectDto;
import org.faddy.community_feed.infra.s3.exception.S3Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    /**
     * 파일을 S3에 업로드
     *
     * @param file 업로드할 파일
     * @param dirPath S3 내부 디렉토리 경로
     * @return S3에 업로드된 객체 정보
     */
    public S3ObjectDto uploadFile(MultipartFile file, String dirPath) {
        String originalFilename = file.getOriginalFilename();

        try {
            return uploadFile(
                file.getInputStream(),
                file.getSize(),
                file.getContentType(),
                originalFilename,
                dirPath
            );
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new S3Exception.FileUploadException(e.getMessage(), e);
        }
    }

    /**
     * 바이트 배열로부터 S3에 파일을 업로드
     *
     * @param bytes 파일 데이터
     * @param contentType 컨텐츠 타입
     * @param filename 파일명
     * @param dirPath S3 내부 디렉토리 경로
     * @return S3에 업로드된 객체 정보
     */
    public S3ObjectDto uploadFile(byte[] bytes, String contentType, String filename, String dirPath) {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return uploadFile(is, bytes.length, contentType, filename, dirPath);
        } catch (IOException e) {
            log.error("Failed to upload bytes to S3: {}", e.getMessage());
            throw new S3Exception.FileUploadException(e.getMessage(), e);
        }
    }

    /**
     * InputStream으로부터 S3에 파일을 업로드
     *
     * @param inputStream 파일 데이터 스트림
     * @param fileSize 파일 크기
     * @param contentType 컨텐츠 타입
     * @param originalFilename 원본 파일명
     * @param dirPath S3 내부 디렉토리 경로
     * @return S3에 업로드된 객체 정보
     */
    public S3ObjectDto uploadFile(InputStream inputStream, long fileSize, String contentType,
        String originalFilename, String dirPath) {

        // 파일명 생성 (UUID + 원본 확장자)
        String key = generateS3Key(originalFilename, dirPath);

        try {
            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileSize);
            metadata.setContentType(contentType);

            // 객체 업로드 요청 생성
            PutObjectRequest request = new PutObjectRequest(
                bucketName,
                key,
                inputStream,
                metadata
            );

            // ACL 설정 제거 - 버킷이 ACL을 지원하지 않음
            // request.withCannedAcl(CannedAccessControlList.PublicRead);

            // S3에 업로드
            amazonS3.putObject(request);

            // 객체 URL 생성
            String url = amazonS3.getUrl(bucketName, key).toString();

            log.info("File uploaded to S3 successfully. Key: {}, URL: {}", key, url);

            // 업로드된 객체 정보 반환
            return S3ObjectDto.builder()
                .key(key)
                .url(url)
                .originalFilename(originalFilename)
                .contentType(contentType)
                .size(fileSize)
                .bucketPath(key) // 명시적으로 S3 키 저장
                .build();
        } catch (Exception e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new S3Exception.FileUploadException(e.getMessage(), e);
        }
    }




    /**
     * S3에서 파일을 삭제
     *
     * @param key 삭제할 파일의 S3 키
     */
    public void deleteFile(String key) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            log.info("File deleted from S3 successfully. Key: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage());
            throw new S3Exception.FileDeleteException(e.getMessage(), e);
        }
    }

    /**
     * S3에서 파일의 존재 여부를 확인
     *
     * @param key 확인할 파일의 S3 키
     * @return 파일 존재 여부
     */
    public boolean doesFileExist(String key) {
        return amazonS3.doesObjectExist(bucketName, key);
    }

    /**
     * S3 객체 키를 생성
     *
     * @param originalFilename 원본 파일명
     * @param dirPath S3 내부 디렉토리 경로
     * @return 생성된 S3 키
     */
    private String generateS3Key(String originalFilename, String dirPath) {
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFilename);

        // 디렉토리 경로 끝에 슬래시 추가
        if (dirPath != null && !dirPath.isEmpty() && !dirPath.endsWith("/")) {
            dirPath += "/";
        }

        return dirPath + uuid + extension;
    }

    /**
     * 파일 확장자를 추출
     *
     * @param filename 파일명
     * @return 파일 확장자 (점 포함)
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}