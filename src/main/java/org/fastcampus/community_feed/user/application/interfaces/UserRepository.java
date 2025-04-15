package org.fastcampus.community_feed.user.application.interfaces;

import org.fastcampus.community_feed.user.domain.User;

public interface UserRepository {

    User save(User user);

    User findById(Long id);
}
