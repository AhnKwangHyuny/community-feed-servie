package org.faddy.community_feed.common.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PagingTest {

    /**
     *  첫 페이징 테스트
     * */
    @Test
    void givenPagingIndexIsOne_whenGetOffset_thenShouldBeReturns0() {
        //given
        Pageable pageable = new Pageable(1, 10);

        //when
        int offset = pageable.getOffset();
        int limit = pageable.getLimit();

        //then
        Assertions.assertEquals(0, offset);
        Assertions.assertEquals(10, limit);
    }

    /**
     * 두 번째 페이징
     * */
    @Test
    void givenPagingIndexIsTwo_whenGetOffset_thenShouldBeReturns10() {
        //given
        Pageable pageable = new Pageable(2, 10);

        //when
        int offset = pageable.getOffset();
        int limit = pageable.getLimit();

        //then
        Assertions.assertEquals(10, offset);
        Assertions.assertEquals(10, limit);
    }


}
