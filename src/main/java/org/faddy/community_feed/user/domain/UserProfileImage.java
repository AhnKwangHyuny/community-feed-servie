package org.faddy.community_feed.user.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.ImageType;

@Getter
public class UserProfileImage extends BaseImage {
    private final User user;
    private final boolean isActive;
    private final LocalDateTime createdAt;

    public UserProfileImage(
        Long id,
        String url,
        String originalFilename,
        String contentType,
        Long size,
        User user,
        boolean isActive,
        LocalDateTime createdAt) {
        super(id, url, originalFilename, contentType, size, ImageType.PROFILE);
        this.user = user;
        this.isActive = isActive;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
