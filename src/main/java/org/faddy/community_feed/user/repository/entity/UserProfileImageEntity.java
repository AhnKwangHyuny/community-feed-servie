package org.faddy.community_feed.user.repository.entity;

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

@Entity
@Table(name = "community_user_profile_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileImageEntity extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private ImageEntity image;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;
}