package org.fastcampus.community_feed.post.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.fastcampus.community_feed.post.application.interfaces.LikeRepository;
import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.domain.comment.Comment;
import org.fastcampus.community_feed.user.domain.User;

public class FakeLikeRepository implements LikeRepository {

    Map<Post, Set<User>> postLikes = new HashMap<>();
    Map<Comment, Set<User>> commentLikes = new HashMap<>();

    @Override
    public boolean checkLike(Post post, User user) {
        if (postLikes.get(post) == null) {
            return false;
        }
        return postLikes.get(post).contains(user);
    }

    @Override
    public boolean checkLike(Comment post, User user) {
        if (commentLikes.get(post) == null) {
            return false;
        }
        return commentLikes.get(post).contains(user);
    }

    @Override
    public void like(Post post, User user) {
        Set<User> users = postLikes.get(post);
        if (users == null) {
            users = new HashSet<>();
        }
        users.add(user);
        postLikes.put(post, users);
    }

    @Override
    public void unlike(Post post, User user) {
        Set<User> users = postLikes.get(post);
        if (users == null) {
            return;
        }
        users.remove(user);
        postLikes.put(post, users);
    }

    @Override
    public void like(Comment comment, User user) {
        Set<User> users = commentLikes.get(comment);
        if (users == null) {
            users = new HashSet<>();
        }
        users.add(user);
        commentLikes.put(comment, users);
    }

    @Override
    public void unlike(Comment comment, User user) {
        Set<User> users = commentLikes.get(comment);
        if (users == null) {
            return;
        }
        users.remove(user);
        commentLikes.put(comment, users);
    }
}
