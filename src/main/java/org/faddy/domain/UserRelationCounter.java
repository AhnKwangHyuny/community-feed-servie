package org.faddy.domain;

public class UserRelationCounter {
    private int count;

    public UserRelationCounter(int count) {
        this.count = count;
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        if(count == 0) {
            throw new IllegalArgumentException("팔로우, 팔로잉 수는 0 이하로 내려갈 수 없습니다.");
        }

        count--;
    }
}
