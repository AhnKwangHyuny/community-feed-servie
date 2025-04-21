package org.faddy.community_feed.message.application.interfaces;

import org.faddy.community_feed.user.domain.User;

public interface MessageRepository {
    void sendLikeMessage(User sendUser, User targetUser);
}
