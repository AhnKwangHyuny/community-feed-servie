package org.faddy.community_feed.image.domain;

public interface Image {
    String getUrl();
    String getOriginalFilename();
    String getContentType();
    Long getSize();
    ImageType getType();
}