package org.faddy.community_feed.post.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.common.principal.AuthPrincipal;
import org.faddy.community_feed.common.principal.UserPrincipal;
import org.faddy.community_feed.common.ui.Response;
import org.faddy.community_feed.post.application.dto.GetPostContentResponseDto;
import org.faddy.community_feed.post.repository.post_queue.UserPostQueueQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final UserPostQueueQueryRepository userPostQueueQueryRepository;

    @GetMapping
    public Response<List<GetPostContentResponseDto>> getPostFeedList(@AuthPrincipal UserPrincipal user, Long lastContentId) {
        List<GetPostContentResponseDto> contentResponse = userPostQueueQueryRepository.getContentResponse(user.getUserId(), lastContentId);
        return Response.ok(contentResponse);
    }
}
