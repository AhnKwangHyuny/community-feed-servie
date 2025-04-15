package org.fastcampus.community_feed.post.repository.post_queue;

import java.util.List;
import org.fastcampus.community_feed.post.application.dto.GetPostContentResponseDto;

public interface UserPostQueueQueryRepository {
    List<GetPostContentResponseDto> getContentResponse(Long userId, Long lastContentId);
}
