package org.faddy.community_feed.post.application;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.ImageStatus;
import org.faddy.community_feed.post.application.interfaces.PostRepository;
import org.faddy.community_feed.post.application.interfaces.PostThumbnailRepository;
import org.faddy.community_feed.post.application.service.PostImageService;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.image.PostThumbnail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Slf4j
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final ImageRepository<BaseImage> imageRepository;
    private final PostRepository postRepository;
    private final PostThumbnailRepository postThumbnailRepository;

    @Override
    @Transactional
    public String attachImagesToPost(List<Long> imageIds, Long postId) {
        if (imageIds == null || imageIds.isEmpty()) {
            return null;
        }

        log.info("Attaching {} images to post: {}", imageIds.size(), postId);

        // 도메인 객체 조회
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        // 이미지 도메인 객체 목록 조회
        List<BaseImage> images = imageRepository.findByIds(imageIds);
        if (images.size() != imageIds.size()) {
            throw new IllegalArgumentException("Some images were not found");
        }

        String mainImageUrl = null;
        List<PostThumbnail> thumbnails = new ArrayList<>();

        // 이미지 상태 변경 및 썸네일 생성
        for (int i = 0; i < images.size(); i++) {
            BaseImage image = images.get(i);

            try {
                // 도메인 객체에서 상태 변경 - 수정된 메서드 사용
                BaseImage updatedImage = image.withStatus(ImageStatus.PERMANENT);
                BaseImage savedImage = imageRepository.save(updatedImage);

                // 썸네일 생성
                boolean isMain = (i == 0);
                PostThumbnail thumbnail = new PostThumbnail(
                    savedImage.getId(),
                    savedImage.getUrl(),
                    savedImage.getOriginalFilename(),
                    savedImage.getContentType(),
                    savedImage.getSize(),
                    post,
                    i,
                    isMain
                );

                thumbnails.add(thumbnail);

                // 메인 이미지 URL 저장
                if (isMain) {
                    mainImageUrl = savedImage.getUrl();
                }
            } catch (Exception e) {
                log.error("Failed to attach image {} to post {}: {}", image.getId(), postId, e.getMessage(), e);
                // 계속 진행하고 실패한 이미지 로깅
            }
        }

        // 썸네일 일괄 저장
        for (PostThumbnail thumbnail : thumbnails) {
            try {
                postThumbnailRepository.save(thumbnail);
            } catch (Exception e) {
                log.error("Failed to save thumbnail for image ID {}: {}",
                    thumbnail.getId(), e.getMessage(), e);
            }
        }

        return mainImageUrl;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getPostImageIds(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            return List.of();
        }

        return postThumbnailRepository.findByPost(post).stream()
            .map(PostThumbnail::getId)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePostImages(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            return;
        }

        // 게시물에 연결된 썸네일 조회
        List<PostThumbnail> thumbnails = postThumbnailRepository.findByPost(post);

        // 이미지 상태 변경
        for (PostThumbnail thumbnail : thumbnails) {
            Long imageId = thumbnail.getId();
            Optional<BaseImage> imageOpt = imageRepository.findById(imageId);
            if (imageOpt.isPresent()) {
                BaseImage image = imageOpt.get();
                // 도메인 객체에서 상태 변경
                BaseImage updatedImage = image.withStatus(ImageStatus.DELETED);
                imageRepository.save(updatedImage);
            }
        }

        // 썸네일 삭제
        postThumbnailRepository.deleteByPost(post);
    }

    @Override
    @Transactional(readOnly = true)
    public String getMainImageUrl(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            return null;
        }

        return postThumbnailRepository.findMainThumbnailByPost(post)
            .map(PostThumbnail::getUrl)
            .orElse(null);
    }
}