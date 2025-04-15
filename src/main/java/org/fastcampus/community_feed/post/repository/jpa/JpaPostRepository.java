package org.fastcampus.community_feed.post.repository.jpa;

import java.util.List;
import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.repository.entity.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p WHERE p.author.id = :authorId")
    List<PostEntity> findAllPostIdsByAuthorId(Long authorId);

    @Modifying
    @Query("UPDATE PostEntity p "
            + "SET p.likeCount = :#{#post.getLikeCount()} "
            + "WHERE p.id = :#{#post.getId()}")
    void updateLikeCount(Post post);

    @Modifying
    @Query("UPDATE PostEntity p "
            + "SET p.content = :#{#post.getContentText()},"
            + "p.state = :#{#post.getState()},"
            + "p.updDt = now() "
            + "WHERE p.id = :#{#post.getId()}")
    void updatePost(Post post);

    @Modifying
    @Query("UPDATE PostEntity p SET p.commentCounter = p.commentCounter + 1 WHERE p.id = :postId")
    void increaseCommentCounter(Long postId);
}
