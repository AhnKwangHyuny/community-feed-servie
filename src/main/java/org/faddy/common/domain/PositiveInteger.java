package org.faddy.common.domain;

// 좋아요, 팔로우 수 등 비즈니스로직 관리
public class PositiveInteger {

    private int count;

    public PositiveInteger() {
        this.count = 0;
    }

    public PositiveInteger(int count) {
        this.count = count;
    }

    public void increase() {
        count += 1;
    }

    public void decrease() {
        if (count == 0) {
            throw new IllegalArgumentException();
        }

        count -= 1;
    }

    public int getCount() {
        return this.count;
    }
}
