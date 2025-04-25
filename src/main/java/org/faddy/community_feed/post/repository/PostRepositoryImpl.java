package org.faddy.community_feed.post.repository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.post.application.interfaces.PostRepository;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.image.PostThumbnail;
import org.faddy.community_feed.post.repository.entity.post.PostEntity;
import org.faddy.community_feed.post.repository.jpa.JpaPostRepository;
import org.faddy.community_feed.post.repository.post_queue.interfaces.UserPostQueueCommandRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JpaPostRepository jpaPostRepository;
    private final UserPostQueueCommandRepository queueRepository;

    @Override
    public Post findById(Long id) {
        PostEntity postEntity = jpaPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return postEntity.toPost();
    }

    @Override
    @Transactional
    public Post save(Post post) {
        if (post.getId() != null) {
            jpaPostRepository.updatePost(post);
            return post;
        }
        PostEntity postEntity = jpaPostRepository.save(new PostEntity(post));
        return postEntity.toPost();
    }

    @Override
    public Post publish(Post post) {
        PostEntity postEntity = jpaPostRepository.save(new PostEntity(post));
        queueRepository.publishPost(postEntity);
        return postEntity.toPost();
    }

    @Override
    public Post findByIdWithThumbnails(Long id) {
        PostEntity postEntity = jpaPostRepository.findByIdWithThumbnails(id)
            .orElseThrow(() -> new IllegalArgumentException("Post를 찾을 수 없습니다."));

        Post post = postEntity.toPost();

        // 썸네일 정보 매핑
        if (postEntity.getThumbnails() != null && !postEntity.getThumbnails().isEmpty()) {
            List<PostThumbnail> thumbnails = postEntity.getThumbnails().stream()
                .map(thumbnail -> new PostThumbnail(
                    thumbnail.getImage().getId(),
                    thumbnail.getImage().getUrl(),
                    thumbnail.getImage().getOriginalFilename(),
                    thumbnail.getImage().getContentType(),
                    thumbnail.getImage().getSize(),
                    post,
                    thumbnail.getDisplayOrder(),
                    thumbnail.isMain()
                ))
                .collect(Collectors.toList());

            // 이미 수정된 Post 클래스에 맞게 설정
            post.updateThumbnailList(thumbnails);
        }

        return post;
    }
}
