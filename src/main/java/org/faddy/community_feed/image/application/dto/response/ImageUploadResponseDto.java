package org.faddy.community_feed.image.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUploadResponseDto {
    private Long id;
    private String url;
    private String filename;
    private String contentType;
    private Long size;
    private String type;
    private String bucketPath;
}