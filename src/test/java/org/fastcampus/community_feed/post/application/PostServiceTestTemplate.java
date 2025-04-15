package org.fastcampus.community_feed.post.application;

import org.fastcampus.community_feed.common.FakeObjectFactory;
import org.fastcampus.community_feed.post.application.dto.CreatePostRequestDto;
import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.domain.PostPublicationState;
import org.fastcampus.community_feed.user.application.UserService;
import org.fastcampus.community_feed.user.application.dto.CreateUserRequestDto;
import org.fastcampus.community_feed.user.domain.User;

public class PostServiceTestTemplate {

    final UserService userService = FakeObjectFactory.getUserService();
    final PostService postService = FakeObjectFactory.getPostService();

    final User user = userService.createUser(new CreateUserRequestDto("user1", null));
    final User otherUser = userService.createUser(new CreateUserRequestDto("user1", null));

    CreatePostRequestDto dto = new CreatePostRequestDto(user.getId(), "this is test content", PostPublicationState.PUBLIC);
    final Post post = postService.createPost(dto);
}
