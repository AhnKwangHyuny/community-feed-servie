package org.faddy.User.application.interfae;

import org.faddy.User.domain.User;

public interface UserRelationRepository {
    boolean isFollowUser(User user , User targetUser);
    void save(User user , User targetUser);
    void delete(User user , User targetUser);
}
