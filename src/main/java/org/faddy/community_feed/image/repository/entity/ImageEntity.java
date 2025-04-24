package org.faddy.community_feed.image.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.common.repository.TimeBaseEntity;
import org.faddy.community_feed.image.domain.ImageStatus;
import org.faddy.community_feed.image.domain.ImageType;

@Entity
@Table(name = "community_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "content_type")
    private String contentType;

    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageStatus status = ImageStatus.TEMPORARY;

    @Column(name = "bucket_path")
    private String bucketPath;  // S3 내 경로

    public void setStatus(ImageStatus status) {
        this.status = status;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }
}