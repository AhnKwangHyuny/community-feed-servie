package org.faddy.community_feed.admin.ui.dto.posts;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.common.domain.Pageable;


@NoArgsConstructor
@AllArgsConstructor
public class GetPostTableRequestDto extends Pageable {

    private String postId;

    // 문자열 "null"을 null로 변환하는 커스텀 세터
    public Long getPostIdAsLong() {
        if (postId == null || postId.equals("null") || postId.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(postId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getPostId() {
        return this.postId;
    }
}
