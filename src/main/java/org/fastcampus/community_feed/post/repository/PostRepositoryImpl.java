package org.fastcampus.community_feed.post.repository;

import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.post.application.interfaces.PostRepository;
import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.repository.entity.post.PostEntity;
import org.fastcampus.community_feed.post.repository.jpa.JpaPostRepository;
import org.fastcampus.community_feed.post.repository.post_queue.UserPostQueueCommandRepository;
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
}
