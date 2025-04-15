package org.fastcampus.community_feed.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.fastcampus.community_feed.common.domain.PositiveIntegerCounter;
import org.junit.jupiter.api.Test;



class PositiveIntegerCounterTest {

    @Test
    void givenCreatedWhenIncreaseThenCountIsOne() {
        // given
        PositiveIntegerCounter count = new PositiveIntegerCounter();

        // when
        count.increase();

        // then
        assertEquals(1, count.getCount());
    }

    @Test
    void givenCreatedAndLikedWhenDecreaseThenCountIsOne() {
        // given
        PositiveIntegerCounter count = new PositiveIntegerCounter();
        count.increase();

        // when
        count.decrease();

        // then
        assertEquals(0, count.getCount());
    }

    @Test
    void givenCreatedWhenDecreaseThenCountIsZero() {
        // given
        PositiveIntegerCounter count = new PositiveIntegerCounter();

        // when
        count.decrease();

        // then
        assertEquals(0, count.getCount());
    }

}
