package org.fastcampus.community_feed.post.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.fastcampus.community_feed.post.domain.content.Content;
import org.fastcampus.community_feed.user.domain.User;
import org.fastcampus.community_feed.user.domain.UserInfo;
import org.junit.jupiter.api.Test;

class PostTest {

    private final User user = new User(1L, new UserInfo("name", "url"));
    private final User otherUser = new User(2L, new UserInfo("name", "url"));

    private final Post post = new Post(1L, user, "content");

    @Test
    void givenPostCreatedWhenLikeThenLikeCountShouldBe1() {
        // when
        post.like(otherUser);

        // then
        assertEquals(1, post.getLikeCount());
    }

    @Test
    void givenPostCreatedWhenLikeByOtherUserThenThrowException() {
        // when, then
        assertThrows(IllegalArgumentException.class, () -> post.like(user));
    }

    @Test
    void givenPostCreatedAndLikeWhenUnlikeThenLikeCountShouldBe0() {
        // given
        post.like(otherUser);

        // when
        post.unlike();

        // then
        assertEquals(0, post.getLikeCount());
    }

    @Test
    void givenPostCreatedWhenUnlikeThenLikeCountShouldBe0() {
        // when
        post.unlike();

        // then
        assertEquals(0, post.getLikeCount());
    }


    @Test
    void givenPostCreatedWhenUpdateContentThenContentShouldBeUpdated() {
        // given
        String newPostContent = "new content";

        // when
        post.updateContent(user, newPostContent, null);

        // then
        Content content = post.getContent();
        assertEquals(newPostContent, content.getContentText());
    }

    @Test
    void givenPostCreatedWhenUpdateContentByOtherUserThenThrowException() {
        // given
        String newPostContent = "new content";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> post.updateContent(otherUser, newPostContent, null));
    }
}
