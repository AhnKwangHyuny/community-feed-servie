package org.faddy.community_feed.auth.application.interfaces;

import org.faddy.community_feed.auth.domain.UserAuth;
import org.faddy.community_feed.user.domain.User;

public interface UserAuthRepository {
    Long registerUser(UserAuth userAuth, User user);
    UserAuth findByEmail(String email);
}
