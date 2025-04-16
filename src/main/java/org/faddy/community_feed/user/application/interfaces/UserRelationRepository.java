package org.faddy.community_feed.user.application.interfaces;

import org.faddy.community_feed.user.domain.User;

public interface UserRelationRepository {

    boolean isAlreadyFollow(User user, User targetUser);
    void save(User user, User targetUser);
    void delete(User user, User targetUser);
}
