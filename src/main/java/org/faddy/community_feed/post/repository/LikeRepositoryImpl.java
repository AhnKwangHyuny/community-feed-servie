package org.faddy.community_feed.post.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.message.application.interfaces.MessageRepository;
import org.faddy.community_feed.post.application.interfaces.LikeRepository;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.comment.Comment;
import org.faddy.community_feed.post.repository.entity.like.LikeEntity;
import org.faddy.community_feed.post.repository.jpa.JpaCommentRepository;
import org.faddy.community_feed.post.repository.jpa.JpaLikeRepository;
import org.faddy.community_feed.post.repository.jpa.JpaPostRepository;
import org.faddy.community_feed.user.domain.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Primary
public class LikeRepositoryImpl implements LikeRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final JpaLikeRepository jpaLikeRepository;
    private final MessageRepository messageRepository;

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
        messageRepository.sendLikeMessage(user, post.getAuthor());
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
