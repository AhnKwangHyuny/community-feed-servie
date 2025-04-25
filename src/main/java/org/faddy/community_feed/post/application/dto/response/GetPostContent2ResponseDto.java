package org.faddy.community_feed.post.application.dto.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.faddy.community_feed.post.domain.PostPublicationState;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostContent2ResponseDto extends GetContentResponseDto {
    private Integer commentCount;
    private String thumbnailUrl;
    private List<String> images;
//    private Integer viewCount;    // 조회수
    private PostPublicationState state; // 게시물 상태 (PUBLIC, PRIVATE 등)
}
