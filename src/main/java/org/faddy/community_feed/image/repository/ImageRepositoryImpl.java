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
public class ImageRepositoryImpl<T extends Image> implements ImageRepository<T> {

    private final JpaImageRepository jpaImageRepository;
    private final Class<T> domainClass;

    public ImageRepositoryImpl(JpaImageRepository jpaImageRepository, Class<T> domainClass) {
        this.jpaImageRepository = jpaImageRepository;
        this.domainClass = domainClass;
    }

    // 타입 안전한 캐스팅을 위한 메서드
    @SuppressWarnings("unchecked")
    protected T safeCast(BaseImage baseImage) {
        if (domainClass.isInstance(baseImage)) {
            return (T) baseImage;
        }
        throw new ClassCastException("Cannot cast " + baseImage.getClass() + " to " + domainClass);
    }


    @Override
    @Transactional
    public T save(T image) {
        ImageEntity imageEntity = convertToEntity(image);
        ImageEntity savedEntity = jpaImageRepository.save(imageEntity);
        return (T) convertToDomain(savedEntity);
    }

    @Override
    public Optional<T> findById(Long id) {
        return jpaImageRepository.findById(id)
            .map(entity -> (T) convertToDomain(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaImageRepository.deleteById(id);
    }

    @Override
    public List<T> findAll() {
        return jpaImageRepository.findAll().stream()
            .map(entity -> (T) convertToDomain(entity))
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