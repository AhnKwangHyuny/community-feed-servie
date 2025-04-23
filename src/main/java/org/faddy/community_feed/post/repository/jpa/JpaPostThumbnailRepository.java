package org.faddy.community_feed.post.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.faddy.community_feed.post.repository.entity.image.PostThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaPostThumbnailRepository extends JpaRepository<PostThumbnailEntity, Long> {

    @Query("SELECT t FROM PostThumbnailEntity t JOIN FETCH t.image WHERE t.post.id = :postId ORDER BY t.displayOrder ASC")
    List<PostThumbnailEntity> findByPostIdOrderByDisplayOrderAsc(@Param("postId") Long postId);

    @Query("SELECT t FROM PostThumbnailEntity t JOIN FETCH t.image WHERE t.post.id = :postId AND t.isMain = true")
    Optional<PostThumbnailEntity> findByPostIdAndIsMainTrue(@Param("postId") Long postId);

    @Query("SELECT t FROM PostThumbnailEntity t WHERE t.post.id = :postId")
    List<PostThumbnailEntity> findByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE FROM PostThumbnailEntity t WHERE t.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}