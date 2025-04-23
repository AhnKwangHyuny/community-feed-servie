package org.faddy.community_feed.post.domain.image;

import lombok.Builder;
import lombok.Getter;
import org.faddy.community_feed.image.domain.BaseImage;
import org.faddy.community_feed.image.domain.ImageType;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.repository.entity.image.PostThumbnailEntity;

@Getter
@Builder
public class PostThumbnail extends BaseImage {
    private final Post post;
    private final int displayOrder;
    private final boolean isMain;

    public PostThumbnail(
        Long id,
        String url,
        String originalFilename,
        String contentType,
        Long size,
        Post post,
        int displayOrder,
        boolean isMain) {
        super(  id , url, originalFilename, contentType, size, ImageType.POST_THUMBNAIL);
        this.post = post;
        this.displayOrder = displayOrder;
        this.isMain = isMain;
    }


    // 비즈니스 메서드
    public PostThumbnail makeMainThumbnail() {
        return new PostThumbnail(
            this.getId(),
            this.getUrl(),
            this.getOriginalFilename(),
            this.getContentType(),
            this.getSize(),
            this.post,
            this.displayOrder,
            true
        );
    }



}
