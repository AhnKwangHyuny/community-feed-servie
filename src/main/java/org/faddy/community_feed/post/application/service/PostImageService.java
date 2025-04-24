package org.faddy.community_feed.post.application.service;

import java.util.List;

public interface PostImageService {
    /**
     * 게시물에 이미지 연결
     * @param imageIds 연결할 이미지 ID 목록
     * @param postId 게시물 ID
     * @return 메인 이미지 URL (있는 경우)
     */
    String attachImagesToPost(List<Long> imageIds, Long postId);

    /**
     * 게시물의 이미지 목록 조회
     * @param postId 게시물 ID
     * @return 이미지 ID 목록
     */
    List<Long> getPostImageIds(Long postId);

    /**
     * 게시물 이미지 삭제
     * @param postId 게시물 ID
     */
    void deletePostImages(Long postId);

    /**
     * 게시물의 메인 이미지 URL 조회
     * @param postId 게시물 ID
     * @return 메인 이미지 URL (없으면 null)
     */
    String getMainImageUrl(Long postId);
}