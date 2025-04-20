package org.faddy.community_feed.admin.ui.dto.posts;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.faddy.community_feed.common.utils.TimeCalculator;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostTableResponseDto {

    @Getter
    private Long postId;

    @Getter
    private Long userId;

    @Getter
    private String userName;

    @Getter
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getCreatedAt() {
        return TimeCalculator.getFormattedDate(createdAt);
    }

    public String getUpdatedAt() {
        return TimeCalculator.getFormattedDate(updatedAt);
    }
}