package org.faddy.community_feed.common.domain;

import lombok.NoArgsConstructor;

public class Pageable {
    private int pageIndex = 1; // 기본값을 1로 설정
    private int pageSize = 10;

    public Pageable() {
        // 기본값이 이미 설정되어 있으므로 추가 작업 필요 없음
    }

    public Pageable(int pageIndex, int pageSize) {
        if(pageIndex < 1) {
            throw new IllegalArgumentException("pageIndex must be greater than 0");
        }

        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (pageIndex - 1) * pageSize;
    }

    public int getLimit() {
        return pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex < 1 ? 1 : pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
