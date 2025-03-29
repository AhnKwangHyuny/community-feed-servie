package org.faddy.post.application.repo.entity;

import org.faddy.User.infrastructure.repo.entity.UserEntity;
import org.faddy.User.domain.User;
import org.faddy.post.domain.Post;
import org.faddy.post.domain.content.PostContent;
import org.faddy.post.domain.content.PostPublicationState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PostEntityTest {

    @Test
    @DisplayName("PostEntity 빌더 패턴으로 객체 생성 테스트")
    void givenPostEntityBuilder_whenBuildEntity_thenShouldCreateEntityWithCorrectValues() {
        // given
        Long id = 1L;
        UserEntity author = Mockito.mock(UserEntity.class);
        String content = "테스트 컨텐츠";
        Integer likeCount = 10;
        PostPublicationState state = PostPublicationState.PUBLIC;

        // when
        PostEntity postEntity = PostEntity.builder()
            .id(id)
            .author(author)
            .content(content)
            .likeCount(likeCount)
            .state(state)
            .build();

        // then
        assertThat(postEntity).isNotNull();
        assertThat(postEntity.getId()).isEqualTo(id);
        assertThat(postEntity.getAuthor()).isEqualTo(author);
        assertThat(postEntity.getContent()).isEqualTo(content);
        assertThat(postEntity.getLikeCount()).isEqualTo(likeCount);
        assertThat(postEntity.getState()).isEqualTo(state);
    }

    @Test
    @DisplayName("Post 도메인 객체로부터 PostEntity 변환 테스트")
    void testFromPostToPostEntity() {
        // given
        Long id = 1L;
        User user = Mockito.mock(User.class);
        PostContent content = Mockito.mock(PostContent.class);
        Integer likeCount = 10;
        PostPublicationState state = PostPublicationState.PUBLIC;

        UserEntity userEntity = Mockito.mock(UserEntity.class);

        Post post = Mockito.mock(Post.class);
        when(post.getId()).thenReturn(id);
        when(post.getAuthor()).thenReturn(user);
        when(post.getContent()).thenReturn(content);
        when(post.getLikeCount()).thenReturn(likeCount);
        when(post.getState()).thenReturn(state);

        try (var mockStatic = Mockito.mockStatic(UserEntity.class)) {
            mockStatic.when(() -> UserEntity.fromUser(user)).thenReturn(userEntity);

            // when
            PostEntity result = PostEntity.fromPost(post);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getAuthor()).isEqualTo(userEntity);
            assertThat(result.getLikeCount()).isEqualTo(likeCount);
            assertThat(result.getState()).isEqualTo(state);
        }
    }

    @Test
    @DisplayName("PostEntity에서 Post 도메인 객체로 변환 테스트")
    void testToPost() {
        // given
        Long id = 1L;
        UserEntity author = Mockito.mock(UserEntity.class);
        User user = Mockito.mock(User.class);
        String content = "테스트 컨텐츠";
        Integer likeCount = 10;
        PostPublicationState state = PostPublicationState.PUBLIC;

        when(author.toUser()).thenReturn(user);

        PostEntity postEntity = PostEntity.builder()
            .id(id)
            .author(author)
            .content(content)
            .likeCount(likeCount)
            .state(state)
            .build();

        // when
        Post post = postEntity.toPost();

        // then
        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(id);
        assertThat(post.getAuthor()).isEqualTo(user);
        assertThat(post.getContentText()).isEqualTo(content);
        assertThat(post.getLikeCount()).isEqualTo(likeCount);
        assertThat(post.getState()).isEqualTo(state);
    }
}