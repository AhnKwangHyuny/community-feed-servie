package org.faddy.community_feed.post.application.interfaces;

import org.faddy.community_feed.post.domain.comment.Comment;

public interface CommentRepository {
    Comment findById(Long id);
    Comment save(Comment comment);
}
