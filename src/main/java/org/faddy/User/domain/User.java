package org.faddy.User.domain;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.faddy.common.domain.PositiveInteger;

@Getter
public class User {

    @Getter
    private final Long id;
    @Getter
    private final UserInfo info;
    private final PositiveInteger followingCounter;
    private final PositiveInteger followerCounter;


    public User(Long id, UserInfo info) {

        if(info == null) {
            throw new IllegalArgumentException("유저 정보가 존재하지 않습니다.");
        }

        this.id = id;
        this.info = info;
        this.followerCounter = new PositiveInteger();
        this.followingCounter = new PositiveInteger();
    }

    @Builder
    public User(Long id, UserInfo info, PositiveInteger followingCounter, PositiveInteger followerCounter) {
        if(info == null) {
            throw new IllegalArgumentException("유저 정보가 존재하지 않습니다.");
        }

        this.id = id;
        this.info = info;
        this.followerCounter = followerCounter != null ? followerCounter : new PositiveInteger();
        this.followingCounter = followingCounter != null ? followingCounter : new PositiveInteger();
    }


    public void follow(User targetUser) {
        // 자기 자신 팔로우 -> 에러 발생
        if (targetUser.equals(this)) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다.");
        }

        this.increaseFollowingCount();
        targetUser.increaseFollowerCount();
    }

    public void unFollow(User targetUser) {
        // 자기 자신 팔로우 -> 에러 발생
        if (targetUser.equals(this)) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다.");
        }

        this.decreaseFollowingCount();
        targetUser.decreaseFollowerCount();
    }


    // java 디미터 법칙 (자신의 소유 객체랑 상호작용) 캡슐화
    private void increaseFollowerCount() {
        this.followerCounter.increase();
    }

    private void decreaseFollowerCount() {
        this.followerCounter.decrease();
    }

    private void increaseFollowingCount() {
        this.followingCounter.increase();
    }

    private void decreaseFollowingCount() {
        this.followingCounter.decrease();
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

    public int getFollowingCount() {
        return this.followingCounter.getCount();
    }

    public int getFollowerCount() {
        return this.followerCounter.getCount();
    }

}
