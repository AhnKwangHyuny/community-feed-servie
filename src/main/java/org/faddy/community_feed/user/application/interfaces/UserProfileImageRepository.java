package org.faddy.community_feed.user.application.interfaces;

import java.util.List;
import java.util.Optional;
import org.faddy.community_feed.image.application.interfaces.ImageRepository;
import org.faddy.community_feed.user.domain.User;
import org.faddy.community_feed.user.domain.UserProfileImage;

public interface UserProfileImageRepository extends ImageRepository<UserProfileImage> {
    Optional<UserProfileImage> findActiveByUser(User user);
    List<UserProfileImage> findAllByUser(User user);
    void deactivateAllByUser(User user);
}