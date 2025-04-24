package org.faddy.community_feed.image.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.ImageStatus;
import org.faddy.community_feed.image.repository.entity.ImageEntity;
import org.faddy.community_feed.image.repository.jpa.JpaImageRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository<BaseImage> {

    private final JpaImageRepository jpaImageRepository;

    @Override
    @Transactional
    public BaseImage save(BaseImage image) {
        ImageEntity imageEntity = convertToEntity(image);
        ImageEntity savedEntity = jpaImageRepository.save(imageEntity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<BaseImage> findById(Long id) {
        return jpaImageRepository.findById(id)
            .map(this::convertToDomain);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaImageRepository.deleteById(id);
    }

    @Override
    public List<BaseImage> findAll() {
        return jpaImageRepository.findAll().stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BaseImage updateStatus(Long imageId, ImageStatus status) {
        Optional<ImageEntity> entityOpt = jpaImageRepository.findById(imageId);
        if (entityOpt.isEmpty()) {
            throw new IllegalArgumentException("Image not found: " + imageId);
        }

        ImageEntity entity = entityOpt.get();
        entity.setStatus(status);
        ImageEntity savedEntity = jpaImageRepository.save(entity);

        return convertToDomain(savedEntity);
    }

    @Override
    public List<BaseImage> findByIds(List<Long> ids) {
        return jpaImageRepository.findAllById(ids).stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }

    protected ImageEntity convertToEntity(BaseImage image) {

        if(image.getStatus() == null) {
            image = image.withStatus(ImageStatus.TEMPORARY);
        }

        return ImageEntity.builder()
            .id(image.getId())
            .url(image.getUrl())
            .originalFilename(image.getOriginalFilename())
            .contentType(image.getContentType())
            .size(image.getSize())
            .type(image.getType())
            .status(image.getStatus())
            // 버킷 경로 명시적 설정
            .bucketPath(extractBucketPathFromUrl(image.getUrl()))
            .build();
    }

    protected BaseImage convertToDomain(ImageEntity entity) {
        BaseImage image = BaseImage.builder()
            .id(entity.getId())
            .url(entity.getUrl())
            .originalFilename(entity.getOriginalFilename())
            .contentType(entity.getContentType())
            .size(entity.getSize())
            .type(entity.getType())
            .build();

        // 상태 설정 수정 - 새 객체 반환
        if (entity.getStatus() != null) {
            return image.withStatus(entity.getStatus());
        }

        return image;
    }

    // URL에서 버킷 경로 추출 헬퍼 메서드
    private String extractBucketPathFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        // 호스트 부분 제거
        String path = url;
        if (url.contains("://")) {
            int hostEndIndex = url.indexOf("/", url.indexOf("://") + 3);
            if (hostEndIndex > 0) {
                path = url.substring(hostEndIndex + 1);
            }
        }

        // 버킷 이름 제거 (첫 번째 경로 세그먼트)
        int firstSlashIndex = path.indexOf("/");
        if (firstSlashIndex >= 0 && firstSlashIndex < path.length() - 1) {
            path = path.substring(firstSlashIndex + 1);
        }

        return path;
    }
}