package org.faddy.community_feed.image.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.Image;
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

    protected ImageEntity convertToEntity(Image image) {
        return ImageEntity.builder()
            .id(null) // ID는 자동 생성
            .url(image.getUrl())
            .originalFilename(image.getOriginalFilename())
            .contentType(image.getContentType())
            .size(image.getSize())
            .type(image.getType())
            .build();
    }

    protected BaseImage convertToDomain(ImageEntity entity) {
        return BaseImage.builder()
            .id(entity.getId())
            .url(entity.getUrl())
            .originalFilename(entity.getOriginalFilename())
            .contentType(entity.getContentType())
            .size(entity.getSize())
            .type(entity.getType())
            .build();
    }
}