package org.faddy.common.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private final UserInfo userInfo = new UserInfo("test", "");
    private  User user1;
    private  User user2;

    @BeforeEach
    void init() {
        user1 = new User(1L , userInfo);
        user2 = new User(2L , userInfo);
    }

    @Test
    void givenRwoUser_whenEqual_thenReturnFalse() {
        // when
        boolean value = user1.equals(user2);

        //then
        assertEquals(false, value);
    }

    @Test
    void givenTwoSameIdUser_whenEqual_thenTrue() {
        //given
        User testUser3 = new User(1L, userInfo);

        //when
        boolean value = testUser3.equals(user1);

        //then
        assertEquals(true , value);
    }

    @Test
    void givenTwoUser_whenHashCode_thenFalse() {
        //when
        boolean value = user1.hashCode() == user2.hashCode();

        //then
        assertEquals(false , value);
    }

    @Test
    void givenTwoSameUser_whenHashCode_thenTrue() {
        //given
        User testUser3 = new User(1L, userInfo);

        //when
        boolean value = user1.hashCode() == testUser3.hashCode();

        //then
        assertEquals(true, value);
    }

    @Test
    void givenTwoUser_whenUser1FollowUser2_thenIncreaseUser2FollowerCountAndUser1FollowingCountOne() {
        //when
        user1.follow(user2);
        int followingCount = user1.getFollowingCount();
        int followerCount = user2.getFollowerCount();

        System.out.println("followerCount = " + followerCount);

        //then
        assertAll(
            () -> assertEquals(1, followingCount, "User1's following count should be 1"),
            () -> assertEquals(1, followerCount, "User2's follower count should be 1")
        );
    }

    @Test
    void givenTwoUser_whenUser1FollowUser2_thenIncreaseUserCount() {
        //when
        user1.follow(user2);
        //then
        assertAll(
            () -> assertEquals(1, user1.getFollowingCount(), "User1's following count should be 1"),
            () -> assertEquals(0 , user1.getFollowerCount() , "User1's follower count should be 0"),
            () -> assertEquals(1, user2.getFollowerCount(), "User2's follower count should be 1"),
            () -> assertEquals(0, user2.getFollowingCount() , "User2's following count should be 0")
        );
    }


    @Test
    void givenTwoUserFollow_whenUnFollow_thenDecreaseUserCount() {
        //given
        user1.follow(user2);

        //then
        user1.unFollow(user2);

        //then
        assertAll(
            () -> assertEquals(0, user1.getFollowingCount(), "user1의 팔로잉 수는 0이어야 합니다"),
            () -> assertEquals(0, user1.getFollowerCount(), "user1의 팔로워 수는 0이어야 합니다"),
            () -> assertEquals(0, user2.getFollowerCount(), "user2의 팔로워 수는 0이어야 합니다"),
            () -> assertEquals(0, user2.getFollowingCount(), "user2의 팔로잉 수는 0이어야 합니다")
        );
    }

}
