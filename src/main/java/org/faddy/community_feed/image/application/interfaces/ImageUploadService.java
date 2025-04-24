package org.faddy.community_feed.image.application.interfaces;

import org.faddy.community_feed.image.application.dto.response.ImageUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageUploadService {

    ImageUploadResponseDto uploadTemporaryImage(MultipartFile file, String imageTypeStr, Long userId);

    List<ImageUploadResponseDto> uploadMultipleTemporaryImages(List<MultipartFile> files, String imageTypeStr, Long userId);

    void deleteTemporaryImage(Long imageId, Long userId);
}
