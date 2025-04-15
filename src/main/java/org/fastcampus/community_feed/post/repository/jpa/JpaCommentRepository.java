package org.fastcampus.community_feed.post.repository.jpa;

import org.fastcampus.community_feed.post.domain.comment.Comment;
import org.fastcampus.community_feed.post.repository.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {
    @Modifying
    @Query("UPDATE CommentEntity c "
            + "SET c.likeCount = :#{#comment.getLikeCount()} "
            + "WHERE c.id = :#{#comment.getId()}")
    void updateLikeCount(Comment comment);

    @Modifying
    @Query("UPDATE CommentEntity c "
            + "SET c.content = :#{#comment.getContentText()},"
            + "c.updDt = now() "
            + "WHERE c.id = :#{#comment.getId()}")
    void updateComment(Comment comment);
}
