package org.faddy.common.domain;

// 좋아요, 팔로우 수 등 비즈니스로직 관리
public class PositiveInteger {

    private int count;

    public PositiveInteger() {
        this.count = 0;
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        if (count == 0) {
            throw new IllegalArgumentException();
        }

        count--;
    }

    public int getLikeCount() {
        return this.count;
    }
}
