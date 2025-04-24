package org.faddy.community_feed.image.application.interfaces;


import java.util.List;
import java.util.Optional;
import org.faddy.community_feed.image.domain.Image;
import org.faddy.community_feed.image.domain.ImageStatus;
import org.faddy.community_feed.image.domain.ImageType;

public interface ImageRepository<T extends Image> {
    T save(T image);
    Optional<T> findById(Long id);
    void deleteById(Long id);
    List<T> findAll();

    T updateStatus(Long imageId, ImageStatus status);
    List<T> findByIds(List<Long> ids);
}