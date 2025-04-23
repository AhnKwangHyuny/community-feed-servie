package org.faddy.community_feed.post.application.interfaces;

import java.util.List;
import java.util.Optional;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.image.PostThumbnail;

public interface PostThumbnailRepository extends ImageRepository<PostThumbnail> {
    List<PostThumbnail> findByPost(Post post);
    Optional<PostThumbnail> findMainThumbnailByPost(Post post);
    void deleteByPost(Post post);
}
