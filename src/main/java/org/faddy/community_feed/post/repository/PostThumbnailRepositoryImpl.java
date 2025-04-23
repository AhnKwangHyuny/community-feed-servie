package org.faddy.community_feed.post.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.repository.entity.ImageEntity;
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
    private final PostRepository postRepository;

    @Override
    @Transactional
    public PostThumbnail save(PostThumbnail thumbnail) {
        try {
            // 새로운 메인 썸네일을 저장하기 전, 기존 메인 썸네일이 있으면 일반 썸네일로 변경
            if (thumbnail.isMain()) {
                jpaPostThumbnailRepository.findByPostIdAndIsMainTrue(thumbnail.getPost().getId())
                    .ifPresent(existingMain -> {
                        existingMain.setMain(false);
                        jpaPostThumbnailRepository.save(existingMain);
                    });
            }

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

            // PostThumbnailEntity 생성 및 저장
            PostThumbnailEntity thumbnailEntity = PostThumbnailEntity.builder()
                .image(ImageEntity.builder()
                    .id(savedImage.getId())
                    .url(savedImage.getUrl())
                    .originalFilename(savedImage.getOriginalFilename())
                    .contentType(savedImage.getContentType())
                    .size(savedImage.getSize())
                    .type(savedImage.getType())
                    .build())
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