package org.faddy.community_feed.post.repository.post_queue.interfaces;

import java.util.List;
import org.faddy.community_feed.post.application.dto.response.GetPostContent2ResponseDto;
import org.springframework.stereotype.Repository;


public interface PostQueryRepository {

    /**
     * 최신순으로 게시물 목록 조회
     */
    List<GetPostContent2ResponseDto> findLatestPosts(Long lastContentId, int pageSize, Long userId);

    /**
     * 오래된순으로 게시물 목록 조회
     */
    List<GetPostContent2ResponseDto> findOldestPosts(Long lastContentId, int pageSize, Long userId);

    /**
     * 좋아요 수 기준으로 인기 게시물 목록 조회
     */
    List<GetPostContent2ResponseDto> findPopularPosts(Long lastContentId, int pageSize, Long userId);

    /**
     * 조회수 기준으로 게시물 목록 조회
     */
    List<GetPostContent2ResponseDto> findMostViewedPosts(Long lastContentId, int pageSize, Long userId);

    /**
     * 댓글 수 기준으로 게시물 목록 조회
     */
    List<GetPostContent2ResponseDto> findMostCommentedPosts(Long lastContentId, int pageSize, Long userId);

    /**
     * 사용자 맞춤 추천 알고리즘에 따라 게시물 목록 조회
     * 추천 점수는 좋아요 수(×3) + 조회수(×1) + 댓글 수(×2)로 계산
     */
    List<GetPostContent2ResponseDto> findRecommendedPosts(Long lastContentId, int pageSize, Long userId);

    /**
     * 특정 사용자가 작성한 게시물 목록을 조회
     */
    List<GetPostContent2ResponseDto> findPostsByUserId(Long userId, Long lastContentId, int pageSize);
}