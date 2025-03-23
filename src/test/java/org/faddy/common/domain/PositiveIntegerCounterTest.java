package org.faddy.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositiveIntegerCounterTest {

    @Test
    void givenCreated_whenIncrease_thenCountIsOne() {
        //given
        PositiveInteger count = new PositiveInteger();

        //when
        count.increase();

        //then
        assertEquals(1, count.getCount());
    }

    @Test
    void givenCreatedAndIncrease_whenDecrease_thenDeCountIsOne() {
        //given
        PositiveInteger count = new PositiveInteger();
        count.increase();

        //when
        count.decrease();

        //then
        assertEquals(0 , count.getCount());
    }
}
