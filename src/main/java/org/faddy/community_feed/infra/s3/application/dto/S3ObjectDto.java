package org.faddy.community_feed.infra.s3.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class S3ObjectDto {
    private String key;
    private String url;
    private String originalFilename;
    private String contentType;
    private Long size;
    private String bucketPath; // S3 경로
}