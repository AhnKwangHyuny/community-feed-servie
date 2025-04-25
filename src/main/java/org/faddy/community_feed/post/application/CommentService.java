package org.faddy.community_feed.post.application;

import org.faddy.community_feed.post.application.dto.request.CreateCommentRequestDto;
import org.faddy.community_feed.post.application.dto.request.LikeRequestDto;
import org.faddy.community_feed.post.application.dto.request.UpdateCommentRequestDto;
import org.faddy.community_feed.post.application.interfaces.CommentRepository;
import org.faddy.community_feed.post.application.interfaces.LikeRepository;
import org.faddy.community_feed.post.application.service.PostService;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.comment.Comment;
import org.faddy.community_feed.user.application.UserService;
import org.faddy.community_feed.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public CommentService(UserService userService, PostService postService, CommentRepository commentRepository, LikeRepository likeRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(CreateCommentRequestDto dto) {
        Post post = postService.getPost(dto.postId());
        User author = userService.getUser(dto.authorId());
        Comment comment = new Comment(null, post, author, dto.content());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, UpdateCommentRequestDto dto) {
        Comment comment = getComment(commentId);
        User author = userService.getUser(dto.authorId());
        comment.updateContent(author, dto.content());
        return commentRepository.save(comment);
    }

    public void likeComment(LikeRequestDto dto) {
        Comment comment = getComment(dto.targetId());
        User user = userService.getUser(dto.userId());

        if (likeRepository.checkLike(comment, user)) {
            return;
        }

        comment.like(user);
        likeRepository.like(comment, user);
    }

    public void unlikeComment(LikeRequestDto dto) {
        Comment comment = getComment(dto.targetId());
        User user = userService.getUser(dto.userId());

        if (likeRepository.checkLike(comment, user)) {
            comment.unlike();
            likeRepository.unlike(comment, user);
        }
    }
}
