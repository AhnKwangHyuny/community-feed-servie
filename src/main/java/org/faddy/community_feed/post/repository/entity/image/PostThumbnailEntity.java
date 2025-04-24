package org.faddy.community_feed.post.repository.entity.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.faddy.community_feed.common.repository.TimeBaseEntity;
import org.faddy.community_feed.image.repository.entity.ImageEntity;
import org.faddy.community_feed.post.repository.entity.post.PostEntity;

@Entity
@Table(name = "community_post_thumbnail")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PostThumbnailEntity extends TimeBaseEntity {

    public PostThumbnailEntity(PostEntity post, ImageEntity image, int displayOrder, boolean isMain) {
        this.post = post;
        this.image = image;
        this.displayOrder = displayOrder;
        this.isMain = isMain;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private ImageEntity image;

    @Column(name = "display_order")
    private int displayOrder;

    private boolean isMain;

    // 설정자 추가
    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }
}
