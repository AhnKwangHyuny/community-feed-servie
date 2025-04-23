package org.faddy.community_feed.image.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BaseImage implements Image {
    private final Long id;
    private final String url;
    private final String originalFilename;
    private final String contentType;
    private final Long size;
    private final ImageType type;

    @Builder
    public BaseImage(Long id, String url, String originalFilename, String contentType, Long size, ImageType type) {
        this.id = id;
        this.url = url;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.type = type;
    }
}
