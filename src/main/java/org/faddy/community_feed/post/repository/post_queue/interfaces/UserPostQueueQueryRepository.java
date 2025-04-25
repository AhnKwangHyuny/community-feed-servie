package org.faddy.community_feed.post.repository.post_queue.interfaces;

import java.util.List;
import org.faddy.community_feed.post.application.dto.response.GetPostContentResponseDto;

public interface UserPostQueueQueryRepository {
    List<GetPostContentResponseDto> getContentResponse(Long userId, Long lastContentId);
}
