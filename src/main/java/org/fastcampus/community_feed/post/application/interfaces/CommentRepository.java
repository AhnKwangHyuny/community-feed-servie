package org.fastcampus.community_feed.post.application.interfaces;

import org.fastcampus.community_feed.post.domain.comment.Comment;

public interface CommentRepository {
    Comment findById(Long id);
    Comment save(Comment comment);
}
