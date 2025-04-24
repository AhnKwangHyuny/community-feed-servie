package org.faddy.community_feed.image.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.application.dto.response.ImageUploadResponseDto;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.image.application.interfaces.ImageUploadService;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.ImageType;
import org.faddy.community_feed.infra.s3.application.S3Service;
import org.faddy.community_feed.infra.s3.application.dto.S3ObjectDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

    private final S3Service s3Service;
    private final ImageRepository<BaseImage> imageRepository;
    private final TransactionTemplate transactionTemplate; // 프로그래밍 방식 트랜잭션

    /**
     * 이미지를 임시 저장소에 업로드
     *
     * @param file 업로드할 이미지 파일
     * @param imageTypeStr 이미지 타입 (POST, PROFILE 등)
     * @param userId 사용자 ID
     * @return 업로드된 이미지 정보
     */
    @Transactional
    public ImageUploadResponseDto uploadTemporaryImage(MultipartFile file, String imageTypeStr, Long userId) {
        // 1. 파일 검증 - 트랜잭션 밖에서 수행
        validateImageFile(file);
        ImageType imageType = parseImageType(imageTypeStr);
        String dirPath = getTempDirectoryPath(imageType, userId);

        // 2. DB 트랜잭션 내에서 처리
        BaseImage savedImage = transactionTemplate.execute(status -> {
            // 2-1. S3 업로드
            S3ObjectDto s3Object = null;
            try {
                s3Object = s3Service.uploadFile(file, dirPath);  // S3 업로드
            } catch (Exception e) {
                // S3 업로드 실패 시 트랜잭션 롤백
                status.setRollbackOnly();
                throw new RuntimeException("S3 업로드 실패: " + e.getMessage(), e);
            }

            // 2-2. DB 저장
            BaseImage image = BaseImage.builder()
                .url(s3Object.getUrl())
                .originalFilename(s3Object.getOriginalFilename())
                .contentType(s3Object.getContentType())
                .size(s3Object.getSize())
                .type(imageType)
                .build();

            // DB 저장
            return imageRepository.save(image);
        });

        // 3. 응답 생성
        return ImageUploadResponseDto.builder()
            .id(savedImage.getId())
            .url(savedImage.getUrl())
            .filename(savedImage.getOriginalFilename())
            .contentType(savedImage.getContentType())
            .size(savedImage.getSize())
            .type(savedImage.getType().name())
            .bucketPath(savedImage.getUrl()) // S3 URL을 그대로 사용
            .build();
    }


    /**
     * 여러 이미지를 한 번에 업로드
     *
     * @param files 업로드할 이미지 파일 목록
     * @param imageTypeStr 이미지 타입 (POST, PROFILE 등)
     * @param userId 사용자 ID
     * @return 업로드된 이미지 정보 목록
     */
    @Transactional
    @Override
    public List<ImageUploadResponseDto> uploadMultipleTemporaryImages(
        List<MultipartFile> files, String imageTypeStr, Long userId) {

        // 이미지 타입 변환
        ImageType imageType = parseImageType(imageTypeStr);

        List<ImageUploadResponseDto> results = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                ImageUploadResponseDto responseDto = uploadTemporaryImage(file, imageTypeStr, userId);
                results.add(responseDto);
            } catch (Exception e) {
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                // 계속 진행하고 성공한 것들만 반환
            }
        }

        return results;
    }

    /**
     * 임시 이미지 삭제
     *
     * @param imageId 삭제할 이미지 ID
     * @param userId 사용자 ID
     */
    @Transactional
    @Override
    public void deleteTemporaryImage(Long imageId, Long userId) {
        // 이미지 조회
        BaseImage image = imageRepository.findById(imageId)
            .orElseThrow(() -> new IllegalArgumentException("Image not found: " + imageId));

        // S3에서 파일 삭제
        // S3 키를 직접 알 수 없으므로 URL에서 추출하거나 별도 저장 필요
        // 여기서는 URL에서 경로 추출 방식 사용
        String key = extractS3KeyFromUrl(image.getUrl());
        s3Service.deleteFile(key);

        // DB에서 이미지 삭제
        imageRepository.deleteById(imageId);
    }

    /**
     * 이미지 타입 문자열을 Enum으로 변환
     *
     * @param imageTypeStr 이미지 타입 문자열
     * @return 이미지 타입 Enum
     */
    private ImageType parseImageType(String imageTypeStr) {
        try {
            return ImageType.valueOf(imageTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid image type: {}", imageTypeStr);
            throw new IllegalArgumentException("Invalid image type: " + imageTypeStr);
        }
    }

    /**
     * 이미지 유효성 검사
     *
     * @param file 검사할 이미지 파일
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Not an image file: " + contentType);
        }

        // 추가 검증 로직 (파일 크기 등)
    }

    /**
     * S3 임시 디렉토리 경로 생성
     *
     * @param imageType 이미지 타입
     * @param userId 사용자 ID
     * @return S3 내부 디렉토리 경로
     */
    private String getTempDirectoryPath(ImageType imageType, Long userId) {
        return "temp/" + imageType.name().toLowerCase() + "/" + userId;
    }

    /**
     * URL에서 S3 키 추출
     *
     * @param url S3 URL
     * @return S3 키
     */
    private String extractS3KeyFromUrl(String url) {
        // URL 형식: https://{bucket}.s3.{region}.amazonaws.com/{key}
        // 또는 https://{endpoint}/{bucket}/{key} (경우에 따라 다름)

        int lastSlashIndex = url.lastIndexOf('/');
        int secondLastSlashIndex = url.lastIndexOf('/', lastSlashIndex - 1);

        return url.substring(secondLastSlashIndex + 1);
    }
}