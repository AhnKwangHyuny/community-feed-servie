package org.faddy.community_feed.post.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.domain.PositiveIntegerCounter;
import org.faddy.community_feed.common.idempotency.annotation.Idempotent;
import org.faddy.community_feed.post.application.dto.request.CreatePostRequestDto;
import org.faddy.community_feed.post.application.dto.request.UpdatePostRequestDto;
import org.faddy.community_feed.post.application.interfaces.LikeRepository;
import org.faddy.community_feed.post.application.interfaces.PostRepository;
import org.faddy.community_feed.post.application.service.PostImageService;
import org.faddy.community_feed.post.application.service.PostService;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.content.PostContent;
import org.faddy.community_feed.user.application.UserService;
import org.faddy.community_feed.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final PostImageService postImageService;


    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postRepository.findById(id);
    }

    @Override
    @Transactional
    public Post createPost(Long userId , CreatePostRequestDto dto) {
        log.info("Creating post for user: {}", userId);

        try {
            // 사용자 조회 및 검증
            User author = userService.getUser(userId);
            if (author == null) {
                throw new IllegalArgumentException("User not found with ID: " + userId );
            }

            // 컨텐츠 유효성 검증
            validateContent(dto.content());

            // 게시물 생성
            Post post;
            if (dto.state() == null) {
                throw new IllegalArgumentException("State is required");
            }

            post = Post.builder()
                .author(author)
                .content(new PostContent(dto.content()))
                .state(dto.state())
                .positiveIntegerCounter(new PositiveIntegerCounter())
                .build();

            // 게시물 저장
            Post savedPost = postRepository.publish(post);
            
            // 이미지 ID가 있는 경우 처리
            if (dto.imageIds() != null && !dto.imageIds().isEmpty()) {
                log.info("Attaching images to post: {}, image count: {}", savedPost.getId(), dto.imageIds().size());
                // 이미지 연결 처리 (PostImageService 활용)
                String mainImageUrl = postImageService.attachImagesToPost(dto.imageIds(), savedPost.getId());
                log.info("Main image URL: {}", mainImageUrl);
            }
            
            return savedPost;
        } catch (Exception e) {
            log.error("Failed to create post for user: {}", userId, e);
            if (e instanceof Exception) {
                throw e;
            }
            throw new IllegalArgumentException("Failed to create post", e);
        }
    }

    @Transactional
    @Override
    public Post updatePost(Long postId, Long userId ,  UpdatePostRequestDto dto) {
        log.info("Updating post: {} for user: {}", postId, userId);

        try {
            // 게시물 및 사용자 조회
            Post post = getPost(postId);
            User user = userService.getUser(userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }

            // 권한 검증
            if (!post.getAuthor().getId().equals(userId)) {
                throw new IllegalArgumentException("You do not have permission to update this post");
            }

            // 컨텐츠 유효성 검증
            validateContent(dto.content());

            // 게시물 업데이트
            post.updateContent(user, dto.content(), dto.state());
            Post updatedPost = postRepository.save(post);
            
            // 이미지 ID 목록이 제공된 경우 처리
            if (dto.imageIds() != null) {
                // 기존 이미지 연결 삭제
//                postImageService.deletePostImages(postId);
                
                // 새 이미지 연결 처리
                if (!dto.imageIds().isEmpty()) {
                    log.info("Updating images for post: {}, image count: {}", postId, dto.imageIds().size());
//                    String mainImageUrl = postImageService.attachImagesToPost(dto.imageIds(), postId);
//                    log.info("Updated main image URL: {}", mainImageUrl);
                }
            }
            
            return updatedPost;
        } catch (Exception e) {
            log.error("Failed to update post: {} for user: {}", postId, userId, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new IllegalArgumentException("Failed to update post", e);
        }
    }

    @Transactional
    @Idempotent
    @Override
    public void likePost(Long userId , Long targetId) {
        log.info("User: {} liking post: {}", userId, targetId);

        try {
            Post post = getPost(targetId);
            User user = userService.getUser(userId);

            // 이미 좋아요를 눌렀는지 확인 (중복 방지)
            if (likeRepository.checkLike(post, user)) {
                log.info("User: {} already liked post: {}", userId, targetId);
                return;
            }

            post.like(user);
            likeRepository.like(post, user);
        } catch (Exception e) {
            log.error("Failed to like post: {} for user: {}", targetId, userId, e);
            if (e instanceof Exception) {
                throw e;
            }
            throw new IllegalArgumentException("Failed to like post", e);
        }
    }

    @Transactional
    @Override
    public void unlikePost(Long userId , Long targetId) {
        log.info("User: {} unliking post: {}", userId, targetId);

        try {
            Post post = getPost(targetId);
            User user = userService.getUser(userId);

            // 좋아요를 누른 적이 있는지 확인
            if (likeRepository.checkLike(post, user)) {
                post.unlike();
                likeRepository.unlike(post, user);
            } else {
                log.info("User: {} has not liked post: {}", userId, targetId);
            }
        } catch (Exception e) {
            log.error("Failed to unlike post: {} for user: {}", targetId, userId, e);
            if (e instanceof Exception) {
                throw e;
            }
            throw new IllegalArgumentException("Failed to unlike post", e);
        }
    }

    // 헬퍼 메서드
    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }

        if (content.length() > 5000) {  // 예시: 최대 5000자 제한
            throw new IllegalArgumentException("Post content exceeds maximum length (5000 characters)");
        }
    }
}
