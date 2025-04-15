package org.fastcampus.community_feed.post.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.post.application.interfaces.LikeRepository;
import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.domain.comment.Comment;
import org.fastcampus.community_feed.post.repository.entity.comment.CommentEntity;
import org.fastcampus.community_feed.post.repository.entity.like.LikeEntity;
import org.fastcampus.community_feed.post.repository.entity.post.PostEntity;
import org.fastcampus.community_feed.post.repository.jpa.JpaCommentRepository;
import org.fastcampus.community_feed.post.repository.jpa.JpaLikeRepository;
import org.fastcampus.community_feed.post.repository.jpa.JpaPostRepository;
import org.fastcampus.community_feed.user.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final JpaLikeRepository jpaLikeRepository;

    @Override
    public boolean checkLike(Post post, User user) {
        LikeEntity entity = new LikeEntity(post, user);
        return jpaLikeRepository.existsById(entity.getId());
    }

    @Override
    public boolean checkLike(Comment comment, User user) {
        LikeEntity entity = new LikeEntity(comment, user);
        return jpaLikeRepository.existsById(entity.getId());
    }

    @Override
    @Transactional
    public void like(Post post, User user) {
        LikeEntity entity = new LikeEntity(post, user);
        entityManager.persist(entity);
        jpaPostRepository.updateLikeCount(post);
    }

    @Override
    @Transactional
    public void like(Comment comment, User user) {
        LikeEntity entity = new LikeEntity(comment, user);
        entityManager.persist(entity);
        jpaCommentRepository.updateLikeCount(comment);
    }

    @Override
    @Transactional
    public void unlike(Post post, User user) {
        LikeEntity entity = new LikeEntity(post, user);
        jpaLikeRepository.deleteById(entity.getId());
        jpaPostRepository.updateLikeCount(post);
    }

    @Override
    @Transactional
    public void unlike(Comment comment, User user) {
        LikeEntity entity = new LikeEntity(comment, user);
        jpaLikeRepository.deleteById(entity.getId());
        jpaCommentRepository.updateLikeCount(comment);
    }
}
