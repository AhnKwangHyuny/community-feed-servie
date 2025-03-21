package org.faddy.domain;

import java.util.Objects;

public class User {
    private final Long id;
    private final UserInfo info;
    private final UserRelationCounter followingCounter;
    private final UserRelationCounter followerCounter;


    public User(Long id, UserInfo info) {
        this.id = id;
        this.info = info;
        this.followerCounter = new UserRelationCounter(0);
        this.followingCounter = new UserRelationCounter(0);
    }

    public void follow(User targetUser) {
        // 자기 자신 팔로우 -> 에러 발생
        if(targetUser.equals(this)) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다.");
        }

        followingCounter.increase();
        targetUser.increaseFollowerCount();
    }

    public void unFollow(User targetUser) {
        // 자기 자신 팔로우 -> 에러 발생
        if(targetUser.equals(this)) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다.");
        }

        followingCounter.decrease();
        targetUser.decreaseFollowerCount();
    }


    // java 디미터 법칙 (자신의 소유 객체랑 상호작용) 캡슐화
    private void increaseFollowerCount() {
        followerCounter.increase();
    }

    private void decreaseFollowerCount() {
        followerCounter.decrease();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
