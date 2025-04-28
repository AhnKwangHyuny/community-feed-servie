package org.faddy.community_feed.post.domain.enumeration;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 게시물 정렬 기준을 정의
 * 기본 정렬 기준은 최신순(LATEST)
 */
@Getter
@RequiredArgsConstructor
public enum SortCriteria {
    /**
     * 최신순 정렬
     */
    LATEST("latest"),

    /**
     * 오래된순 정렬
     */
    OLDEST("oldest"),

    /**
     * 인기순 정렬
     */
    POPULAR("popular"),

    /**
     * 조회수순 정렬
     */
    VIEWS("views"),

    /**
     * 댓글순 정렬
     */
    COMMENTS("comments");

    private final String value;

    public static SortCriteria fromString(String value) {
        return Arrays.stream(SortCriteria.values())
            .filter(criteria -> criteria.getValue().equalsIgnoreCase(value))
            .findFirst()
            .orElse(LATEST);
    }
}