package org.faddy.community_feed.post.repository.post_queue.interfaces;

import java.util.List;
import org.faddy.community_feed.post.application.dto.response.GetPostContentResponseDto;

public interface PostQueryRepository {

    /**
     * 피드용 게시글 목록 조회
     * @param lastContentId 마지막으로 조회한 게시글 ID (무한 스크롤용)
     * @param category 게시글 카테고리(all, popular, my 등)
     * @param pageSize 한 페이지당 게시글 수
     * @return 게시글 목록
     */
    List<GetPostContentResponseDto> getPostFeed(Long lastContentId, String category, int pageSize);

    /**
     * 게시글 상세 정보 조회
     * @param postId 게시글 ID
     * @return 게시글 상세 정보
     */
    GetPostContentResponseDto getPostDetail(Long postId);
}