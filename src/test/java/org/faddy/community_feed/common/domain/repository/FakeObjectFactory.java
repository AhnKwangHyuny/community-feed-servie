package org.faddy.community_feed.common.domain.repository;

import org.faddy.community_feed.post.application.CommentService;
import org.faddy.community_feed.post.application.PostServiceImpl;
import org.faddy.community_feed.post.application.interfaces.CommentRepository;
import org.faddy.community_feed.post.application.interfaces.LikeRepository;
import org.faddy.community_feed.post.application.interfaces.PostRepository;
import org.faddy.community_feed.post.application.service.PostImageService;
import org.faddy.community_feed.post.application.service.PostService;
import org.faddy.community_feed.post.repository.FakeCommentRepository;
import org.faddy.community_feed.post.repository.FakeLikeRepository;
import org.faddy.community_feed.post.repository.FakePostRepository;
import org.faddy.community_feed.user.application.UserRelationService;
import org.faddy.community_feed.user.application.UserService;
import org.faddy.community_feed.user.application.interfaces.UserRelationRepository;
import org.faddy.community_feed.user.application.interfaces.UserRepository;
import org.faddy.community_feed.user.repository.FakeUserRelationRepository;
import org.faddy.community_feed.user.repository.FakeUserRepository;

public class FakeObjectFactory {

    private static final UserRepository fakeUserRepository = new FakeUserRepository();
    private static final UserRelationRepository fakeUserRelationRepository = new FakeUserRelationRepository();
    private static final PostRepository fakePostRepository = new FakePostRepository();
    private static final CommentRepository fakeCommentRepository = new FakeCommentRepository();
    private static final LikeRepository fakeLikeRepository = new FakeLikeRepository();

    private static final UserService userService = new UserService(fakeUserRepository);
    private static final UserRelationService userRelationService = new UserRelationService(fakeUserRelationRepository, userService);
    private static final PostService postService = new PostServiceImpl(userService, fakePostRepository, fakeLikeRepository);
    private static final CommentService commentService = new CommentService(userService, postService, fakeCommentRepository, fakeLikeRepository);

    private FakeObjectFactory() {
    }

    public static UserService getUserService() {
        return userService;
    }

    public static UserRelationService getUserRelationService() {
        return userRelationService;
    }

    public static PostService getPostService() {
        return postService;
    }

    public static CommentService getCommentService() {
        return commentService;
    }
}
