package org.fastcampus.community_feed.user.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.fastcampus.community_feed.common.domain.PositiveIntegerCounter;

@Builder
@AllArgsConstructor
@Getter
public class User {
  private final Long id;
  private final UserInfo userInfo;
  private final PositiveIntegerCounter followingCount;
  private final PositiveIntegerCounter followerCount;

  public User(Long id, UserInfo userInfo) {
    if (userInfo == null) {
      throw new IllegalArgumentException("UserInfo cannot be null");
    }

    this.id = id;
    this.userInfo = userInfo;
    this.followingCount = new PositiveIntegerCounter();
    this.followerCount = new PositiveIntegerCounter();
  }

  public User(String name, String profileImageUrl) {
    this(null, new UserInfo(name, profileImageUrl));
  }

  public void follow(User followee) {
    if (this.equals(followee)) {
      throw new IllegalArgumentException("");
    }

    followingCount.increase();
    followee.increaseFollowerCount();
  }

  public void unfollow(User followee) {
    if (this.equals(followee)) {
      throw new IllegalArgumentException("");
    }

    followingCount.decrease();
    followee.decreaseFollowerCount();
  }

  private void increaseFollowerCount() {
    followerCount.increase();
  }

  private void decreaseFollowerCount() {
    followerCount.decrease();
  }

  public int getFollowingCount() {
    return followingCount.getCount();
  }

  public int getFollowerCount() {
    return followerCount.getCount();
  }

  public String getName() {
    return userInfo.getName();
  }

  public String getProfileImage() {
    return userInfo.getProfileImageUrl();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
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
