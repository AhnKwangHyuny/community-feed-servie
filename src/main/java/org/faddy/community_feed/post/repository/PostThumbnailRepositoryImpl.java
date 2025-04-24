package org.faddy.community_feed.post.repository;

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
import org.faddy.community_feed.post.application.interfaces.PostRepository;
import org.faddy.community_feed.post.application.interfaces.PostThumbnailRepository;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.image.PostThumbnail;
import org.faddy.community_feed.post.repository.entity.image.PostThumbnailEntity;
import org.faddy.community_feed.post.repository.entity.post.PostEntity;
import org.faddy.community_feed.post.repository.jpa.JpaPostThumbnailRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostThumbnailRepositoryImpl implements PostThumbnailRepository {
    private final JpaPostThumbnailRepository jpaPostThumbnailRepository;
    private final ImageRepository<BaseImage> imageRepository;
    private final JpaImageRepository jpaImageRepository;


    @Override
    @Transactional
    public PostThumbnail save(PostThumbnail thumbnail) {
        try {
            // 기존 코드 유지

            // 먼저 기본 이미지를 저장
            BaseImage baseImage = BaseImage.builder()
                .url(thumbnail.getUrl())
                .originalFilename(thumbnail.getOriginalFilename())
                .contentType(thumbnail.getContentType())
                .size(thumbnail.getSize())
                .type(thumbnail.getType())
                .build();

            BaseImage savedImage = imageRepository.save(baseImage);

            // Post 엔티티 참조 가져오기
            PostEntity postEntity = new PostEntity(thumbnail.getPost());

            // 수정: 이미지 엔티티 직접 조회하여 사용 (영속성 컨텍스트 활용)
            ImageEntity imageEntity = jpaImageRepository.getReferenceById(savedImage.getId());

            // PostThumbnailEntity 생성 및 저장
            PostThumbnailEntity thumbnailEntity = PostThumbnailEntity.builder()
                .image(imageEntity) // 참조 객체 직접 사용
                .post(postEntity)
                .displayOrder(thumbnail.getDisplayOrder())
                .isMain(thumbnail.isMain())
                .build();

            PostThumbnailEntity savedEntity = jpaPostThumbnailRepository.save(thumbnailEntity);

            // 저장 성공 시 원본 thumbnail 반환
            return new PostThumbnail(
                savedImage.getId(),
                savedImage.getUrl(),
                savedImage.getOriginalFilename(),
                savedImage.getContentType(),
                savedImage.getSize(),
                thumbnail.getPost(),
                savedEntity.getDisplayOrder(),
                savedEntity.isMain()
            );
        } catch (Exception e) {
            log.error("Failed to save post thumbnail: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save post thumbnail", e);
        }
    }

    @Override
    public Optional<PostThumbnail> findById(Long id) {
        return jpaPostThumbnailRepository.findById(id)
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                PostEntity postEntity = entity.getPost();
                return toDomain(entity, imageEntity, postEntity.toPost());
            });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaPostThumbnailRepository.findById(id).ifPresent(entity -> {
            Long imageId = entity.getImage().getId();

            // 썸네일 엔티티 삭제
            jpaPostThumbnailRepository.delete(entity);

            // 연관된 이미지 삭제
            imageRepository.deleteById(imageId);
        });
    }

    @Override
    public List<PostThumbnail> findAll() {
        return jpaPostThumbnailRepository.findAll().stream()
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                PostEntity postEntity = entity.getPost();
                return toDomain(entity, imageEntity, postEntity.toPost());
            })
            .collect(Collectors.toList());
    }

    // 미구현 메서드 구현
    @Override
    @Transactional
    public PostThumbnail updateStatus(Long imageId, ImageStatus status) {
        // 이미지 상태 업데이트는 BaseImage에 위임
        BaseImage updatedImage = imageRepository.updateStatus(imageId, status);

        // 해당 이미지 ID를 가진 썸네일 찾기
        Optional<PostThumbnailEntity> thumbnailOpt = jpaPostThumbnailRepository.findByImageId(imageId);
        if (thumbnailOpt.isPresent()) {
            PostThumbnailEntity entity = thumbnailOpt.get();
            // 도메인 객체로 변환
            return toDomain(entity, entity.getImage(), entity.getPost().toPost());
        }

        // 썸네일을 찾지 못한 경우
        return null;
    }

    @Override
    public List<PostThumbnail> findByIds(List<Long> ids) {
        return jpaPostThumbnailRepository.findAllById(ids).stream()
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                PostEntity postEntity = entity.getPost();
                return toDomain(entity, imageEntity, postEntity.toPost());
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<PostThumbnail> findByPost(Post post) {
        return jpaPostThumbnailRepository.findByPostIdOrderByDisplayOrderAsc(post.getId()).stream()
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                return toDomain(entity, imageEntity, post);
            })
            .collect(Collectors.toList());
    }

    @Override
    public Optional<PostThumbnail> findMainThumbnailByPost(Post post) {
        return jpaPostThumbnailRepository.findByPostIdAndIsMainTrue(post.getId())
            .map(entity -> {
                ImageEntity imageEntity = entity.getImage();
                return toDomain(entity, imageEntity, post);
            });
    }

    @Override
    @Transactional
    public void deleteByPost(Post post) {
        List<PostThumbnailEntity> thumbnails = jpaPostThumbnailRepository.findByPostId(post.getId());

        // 이미지 ID 리스트 수집
        List<Long> imageIds = thumbnails.stream()
            .map(entity -> entity.getImage().getId())
            .collect(Collectors.toList());

        // 썸네일 엔티티 일괄 삭제 (벌크 연산)
        if (!thumbnails.isEmpty()) {
            jpaPostThumbnailRepository.deleteByPostId(post.getId());

            // 연관된 이미지 개별 삭제
            imageIds.forEach(imageRepository::deleteById);
        }
    }

    private PostThumbnail toDomain(PostThumbnailEntity entity, ImageEntity imageEntity, Post post) {
        return new PostThumbnail(
            imageEntity.getId(),
            imageEntity.getUrl(),
            imageEntity.getOriginalFilename(),
            imageEntity.getContentType(),
            imageEntity.getSize(),
            post,
            entity.getDisplayOrder(),
            entity.isMain()
        );
    }
}