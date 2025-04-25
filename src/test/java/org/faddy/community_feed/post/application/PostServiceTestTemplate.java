package org.faddy.community_feed.post.application;

import org.faddy.community_feed.common.domain.repository.FakeObjectFactory;
import org.faddy.community_feed.post.application.dto.request.CreatePostRequestDto;
import org.faddy.community_feed.post.application.service.PostService;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.PostPublicationState;
import org.faddy.community_feed.user.application.UserService;
import org.faddy.community_feed.user.application.dto.CreateUserRequestDto;
import org.faddy.community_feed.user.domain.User;

public class PostServiceTestTemplate {

    final UserService userService = FakeObjectFactory.getUserService();
    final PostService postService = FakeObjectFactory.getPostService();

    final User user = userService.createUser(new CreateUserRequestDto("user1", null));
    final User otherUser = userService.createUser(new CreateUserRequestDto("user1", null));

    CreatePostRequestDto dto = new CreatePostRequestDto( "this is test content", PostPublicationState.PUBLIC);
    final Post post = postService.createPost(user.getId() , dto);
}
