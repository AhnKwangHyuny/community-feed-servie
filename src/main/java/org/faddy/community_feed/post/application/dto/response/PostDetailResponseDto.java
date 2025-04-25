package org.faddy.community_feed.post.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import org.faddy.community_feed.post.domain.Post;
import org.faddy.community_feed.post.domain.PostPublicationState;
import org.faddy.community_feed.post.domain.image.PostThumbnail;

@Builder
public record PostDetailResponseDto(
    Long id,
    String content,
    Long userId,
    String userName,
    String userProfileImage,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    int likeCount,
    boolean isLikedByMe,
    int commentCount,
    PostPublicationState state,
    List<ThumbnailDto> thumbnails
) {
    // 팩토리 메서드 추가
    public static PostDetailResponseDto from(Post post, int commentCount) {
        return PostDetailResponseDto.builder()
            .id(post.getId())
            .content(post.getContentText())
            .userId(post.getAuthor().getId())
            .userName(post.getAuthor().getName())
            .userProfileImage(post.getAuthor().getProfileImage())
            .createdAt(post.getCreatedAt() != null ? post.getCreatedAt() :
                post.getContent().getDatetimeInfo().getDateTime())
            .updatedAt(post.getContent().getDatetimeInfo().isEdited() ?
                post.getContent().getDatetimeInfo().getDateTime() : null)
            .likeCount(post.getLikeCount())
            .isLikedByMe(post.isLikedByMe())
            .commentCount(commentCount)
            .state(post.getState())
            .thumbnails(post.getThumbnails() != null ?
                post.getThumbnails().stream()
                    .map(ThumbnailDto::from)
                    .collect(Collectors.toList()) :
                List.of())
            .build();
    }

    @Builder
    public record ThumbnailDto(
        Long id,
        String url,
        String originalFilename,
        String contentType,
        int displayOrder,
        boolean isMain
    ) {
        // 팩토리 메서드
        public static ThumbnailDto from(PostThumbnail thumbnail) {
            return ThumbnailDto.builder()
                .id(thumbnail.getId())
                .url(thumbnail.getUrl())
                .originalFilename(thumbnail.getOriginalFilename())
                .contentType(thumbnail.getContentType())
                .displayOrder(thumbnail.getDisplayOrder())
                .isMain(thumbnail.isMain())
                .build();
        }
    }
}