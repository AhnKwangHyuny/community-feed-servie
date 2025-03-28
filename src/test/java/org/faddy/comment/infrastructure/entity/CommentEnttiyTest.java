package org.faddy.comment.infrastructure.entity;

import org.faddy.User.application.repo.entity.UserEntity;
import org.faddy.User.domain.User;
import org.faddy.comment.domain.Comment;
import org.faddy.comment.domain.content.CommentContent;
import org.faddy.post.application.repo.entity.PostEntity;
import org.faddy.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CommentEntityTest {

    @Test
    @DisplayName("CommentEntity 빌더 패턴으로 객체 생성 테스트")
    void given_CommentEntityBuilder_when_BuildEntity_then_ShouldCreateEntityWithCorrectValues() {
        // given
        Long id = 1L;
        UserEntity author = Mockito.mock(UserEntity.class);
        PostEntity post = Mockito.mock(PostEntity.class);
        String content = "테스트 댓글 내용";
        Integer likeCount = 5;

        // when
        CommentEntity commentEntity = CommentEntity.builder()
            .id(id)
            .author(author)
            .post(post)
            .content(content)
            .likeCount(likeCount)
            .build();

        // then
        assertThat(commentEntity).isNotNull();
        assertThat(commentEntity.getId()).isEqualTo(id);
        assertThat(commentEntity.getAuthor()).isEqualTo(author);
        assertThat(commentEntity.getPost()).isEqualTo(post);
        assertThat(commentEntity.getContent()).isEqualTo(content);
        assertThat(commentEntity.getLikeCount()).isEqualTo(likeCount);
    }

    @Test
    @DisplayName("Comment 도메인 객체로부터 CommentEntity 변환 테스트")
    void given_CommentDomainObject_when_ConvertToCommentEntity_then_ShouldCreateCorrectCommentEntity() {
        // given
        Long id = 1L;
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        String contentText = "테스트 댓글 내용";
        Integer likeCount = 5;

        UserEntity userEntity = Mockito.mock(UserEntity.class);
        PostEntity postEntity = Mockito.mock(PostEntity.class);

        Comment comment = Mockito.mock(Comment.class);
        CommentContent commentContent = Mockito.mock(CommentContent.class);

        when(comment.getId()).thenReturn(id);
        when(comment.getAuthor()).thenReturn(user);
        when(comment.getPost()).thenReturn(post);
        when(comment.getContent()).thenReturn(commentContent);
        when(commentContent.getContentText()).thenReturn(contentText);
        when(comment.getLikeCount()).thenReturn(likeCount);

        // UserEntity와 PostEntity의 정적 메서드를 모킹
        try (var userEntityMock = Mockito.mockStatic(UserEntity.class);
            var postEntityMock = Mockito.mockStatic(PostEntity.class)) {

            userEntityMock.when(() -> UserEntity.fromUser(user)).thenReturn(userEntity);
            postEntityMock.when(() -> PostEntity.fromPost(post)).thenReturn(postEntity);

            // when
            CommentEntity result = CommentEntity.fromComment(comment);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getAuthor()).isEqualTo(userEntity);
            assertThat(result.getPost()).isEqualTo(postEntity);
            assertThat(result.getContent()).isEqualTo(contentText);
            assertThat(result.getLikeCount()).isEqualTo(likeCount);
        }
    }

    @Test
    @DisplayName("CommentEntity에서 Comment 도메인 객체로 변환 테스트")
    void given_CommentEntity_when_ConvertToCommentDomain_then_ShouldCreateCorrectCommentDomain() {
        // given
        Long id = 1L;
        UserEntity author = Mockito.mock(UserEntity.class);
        PostEntity post = Mockito.mock(PostEntity.class);
        User user = Mockito.mock(User.class);
        Post postDomain = Mockito.mock(Post.class);
        String content = "테스트 댓글 내용";
        Integer likeCount = 5;

        when(author.toUser()).thenReturn(user);
        when(post.toPost()).thenReturn(postDomain);

        CommentEntity commentEntity = CommentEntity.builder()
            .id(id)
            .author(author)
            .post(post)
            .content(content)
            .likeCount(likeCount)
            .build();

        // when
        Comment comment = commentEntity.toComment();

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(id);
        assertThat(comment.getAuthor()).isEqualTo(user);
        assertThat(comment.getPost()).isEqualTo(postDomain);
        assertThat(comment.getContent().getContentText()).isEqualTo(content);
        assertThat(comment.getLikeCount()).isEqualTo(likeCount);
    }
}