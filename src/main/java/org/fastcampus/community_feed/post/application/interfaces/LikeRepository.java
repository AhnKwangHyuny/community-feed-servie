package org.fastcampus.community_feed.post.application.interfaces;

import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.domain.comment.Comment;
import org.fastcampus.community_feed.user.domain.User;

public interface LikeRepository {
    boolean checkLike(Post post, User user);
    boolean checkLike(Comment comment, User user);
    void like(Post post, User user);
    void like(Comment comment, User user);
    void unlike(Post post, User user);
    void unlike(Comment comment, User user);
}
