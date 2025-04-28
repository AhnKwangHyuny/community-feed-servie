package org.faddy.community_feed.post.application.service;

import java.util.List;
import org.faddy.community_feed.post.application.dto.response.GetPostContent2ResponseDto;


public interface PostFeedService {

    /**
     * 지정된 정렬 기준에 따라 게시물 목록을 조회
     * <p>
     * 지원되는 정렬 기준: latest(최신순), oldest(오래된순), popular(좋아요순),
     * views(조회수순), comments(댓글순)
     * </p>
     *
     * @param sort 정렬 기준 (기본값: latest)
     * @return 지정된 정렬 기준으로 정렬된 게시물 DTO 목록
     */
    List<GetPostContent2ResponseDto> getPostsBySort(Long lastContentId, String sort, Long userId);

    /**
     * 좋아요 수 기준으로 인기 게시물 목록을 조회
     */
    List<GetPostContent2ResponseDto> getPopularPosts(Long lastContentId, Long userId);

    /**
     * 추천 알고리즘에 따라 게시물 목록을 조회
     * 기본 추천 알고리즘은 좋아요(×3) + 조회수(×1) + 댓글 수(×2) 가중치 합산
     */
    List<GetPostContent2ResponseDto> getRecommendedPosts(Long lastContentId, Long userId);

    /**
     * 로그인한 사용자가 작성한 게시물 목록을 조회
     * <p>
     * 사용자 ID가 제공되지 않은 경우 빈 목록을 반환
     * </p>
     * @return 해당 사용자가 작성한 게시물 DTO 목록 (최신순)
     * @throws IllegalArgumentException 사용자 ID가 null인 경우
     */
    List<GetPostContent2ResponseDto> getMyPosts(Long lastContentId, Long userId);
}