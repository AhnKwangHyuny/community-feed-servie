package org.faddy.community_feed.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.ImageStatus;
import org.faddy.community_feed.image.repository.entity.ImageEntity;
import org.faddy.community_feed.user.application.interfaces.UserProfileImageRepository;
import org.faddy.community_feed.user.domain.User;
import org.faddy.community_feed.user.domain.UserProfileImage;
import org.faddy.community_feed.user.repository.entity.UserEntity;
import org.faddy.community_feed.user.repository.entity.UserProfileImageEntity;
import org.faddy.community_feed.user.repository.jpa.JpaUserProfileRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserProfileImageRepositoryImpl implements UserProfileImageRepository {

    private final JpaUserProfileRepository jpaUserProfileRepository;
    private final ImageRepository<BaseImage> imageRepository;

    @Override
    @Transactional
    public UserProfileImage save(UserProfileImage profileImage) {
        try {
            // 새 활성 프로필 이미지를 저장하기 전, 기존 활성 이미지를 비활성화
            if (profileImage.isActive()) {
                jpaUserProfileRepository.deactivateAllByUserId(profileImage.getUser().getId());
            }

            // 먼저 기본 이미지를 저장
            BaseImage baseImage = BaseImage.builder()
                .url(profileImage.getUrl())
                .originalFilename(profileImage.getOriginalFilename())
                .contentType(profileImage.getContentType())
                .size(profileImage.getSize())
                .type(profileImage.getType())
                .build();

            BaseImage savedImage = imageRepository.save(baseImage);

            // User 엔티티 참조 가져오기
            UserEntity userEntity = new UserEntity(profileImage.getUser());

            // UserProfileImageEntity 생성 및 저장
            UserProfileImageEntity profileImageEntity = UserProfileImageEntity.builder()
                .user(userEntity)
                .image(ImageEntity.builder()
                    .id(savedImage.getId())
                    .url(savedImage.getUrl())
                    .originalFilename(savedImage.getOriginalFilename())
                    .contentType(savedImage.getContentType())
                    .size(savedImage.getSize())
                    .type(savedImage.getType())
                    .build())
                .isActive(profileImage.isActive())
                .build();

            UserProfileImageEntity savedEntity = jpaUserProfileRepository.save(profileImageEntity);

            // 저장 성공 시 도메인 객체 반환
            return new UserProfileImage(
                savedEntity.getId(),
                savedImage.getUrl(),
                savedImage.getOriginalFilename(),
                savedImage.getContentType(),
                savedImage.getSize(),
                profileImage.getUser(),
                savedEntity.isActive(),
                savedEntity.getRegDt()
            );
        } catch (Exception e) {
            log.error("Failed to save user profile image: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save user profile image", e);
        }
    }

    @Override
    public Optional<UserProfileImage> findById(Long id) {
        return jpaUserProfileRepository.findById(id)
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                UserEntity userEntity = entity.getUser();
                return toDomain(entity, imageEntity, userEntity.toUser());
            });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaUserProfileRepository.findById(id).ifPresent(entity -> {
            // 연관된 이미지 엔티티의 ID 저장
            Long imageId = entity.getImage().getId();

            // 프로필 이미지 엔티티 삭제
            jpaUserProfileRepository.delete(entity);

            // 연관된 이미지 삭제
            imageRepository.deleteById(imageId);
        });
    }

    @Override
    public List<UserProfileImage> findAll() {
        return jpaUserProfileRepository.findAll().stream()
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                UserEntity userEntity = entity.getUser();
                return toDomain(entity, imageEntity, userEntity.toUser());
            })
            .collect(Collectors.toList());
    }

    @Override
    public UserProfileImage updateStatus(Long imageId, ImageStatus status) {
        return null;
    }

    @Override
    public List<UserProfileImage> findByIds(List<Long> ids) {
        return List.of();
    }

    @Override
    public Optional<UserProfileImage> findActiveByUser(User user) {
        return jpaUserProfileRepository.findActiveByUserId(user.getId())
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                return toDomain(entity, imageEntity, user);
            });
    }

    @Override
    public List<UserProfileImage> findAllByUser(User user) {
        return jpaUserProfileRepository.findAllByUserId(user.getId()).stream()
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                return toDomain(entity, imageEntity, user);
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deactivateAllByUser(User user) {
        jpaUserProfileRepository.deactivateAllByUserId(user.getId());
    }

    private UserProfileImage toDomain(UserProfileImageEntity entity, ImageEntity imageEntity, User user) {
        return new UserProfileImage(
            entity.getId(),
            imageEntity.getUrl(),
            imageEntity.getOriginalFilename(),
            imageEntity.getContentType(),
            imageEntity.getSize(),
            user,
            entity.isActive(),
            entity.getRegDt()
        );
    }
}