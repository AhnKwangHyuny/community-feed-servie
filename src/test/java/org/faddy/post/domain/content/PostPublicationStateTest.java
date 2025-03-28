package org.faddy.post.domain.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PostPublicationStateTest {

    @Test
    @DisplayName("각 상태가 올바른 코드와 설명을 가지고 있는지 확인한다")
    void given_enumValues_when_getCodeAndDescription_then_returnsCorrectValues() {
        // given, when, then
        assertEquals("P", PostPublicationState.PUBLIC.getCode());
        assertEquals("전체 공개", PostPublicationState.PUBLIC.getDescription());

        assertEquals("F", PostPublicationState.ONLY_FOLLOWER.getCode());
        assertEquals("팔로워만", PostPublicationState.ONLY_FOLLOWER.getDescription());

        assertEquals("X", PostPublicationState.PRIVATE.getCode());
        assertEquals("비공개", PostPublicationState.PRIVATE.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
        "P, PUBLIC",
        "F, ONLY_FOLLOWER",
        "X, PRIVATE"
    })
    @DisplayName("코드로부터 올바른 열거형 값을 반환한다")
    void given_validCode_when_getStateFromCode_then_returnsCorrectEnum(String code, PostPublicationState expectedState) {
        // when
        PostPublicationState actualState = PostPublicationState.getStateFromCode(code);

        // then
        assertEquals(expectedState, actualState);
    }

    @Test
    @DisplayName("존재하지 않는 코드에 대해 예외를 발생시킨다")
    void given_invalidCode_when_getStateFromCode_then_throwsException() {
        // given
        String invalidCode = "Z";

        // when, then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PostPublicationState.getStateFromCode(invalidCode)
        );

        assertTrue(exception.getMessage().contains("알 수 없는 게시글 공개 상태 코드입니다"));
        assertTrue(exception.getMessage().contains(invalidCode));
    }

    @Test
    @DisplayName("null 코드에 대해 예외를 발생시킨다")
    void given_nullCode_when_getStateFromCode_then_throwsException() {
        // when, then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PostPublicationState.getStateFromCode(null)
        );

        assertTrue(exception.getMessage().contains("알 수 없는 게시글 공개 상태 코드입니다"));
    }

    @ParameterizedTest
    @MethodSource("provideEnumAndExpectedValues")
    @DisplayName("각 열거형 값이 예상된 코드와 설명을 가지는지 확인한다")
    void given_enumValue_when_getCodeAndDescription_then_matchesExpected(
        PostPublicationState state,
        String expectedCode,
        String expectedDescription) {
        // when, then
        assertEquals(expectedCode, state.getCode());
        assertEquals(expectedDescription, state.getDescription());
    }

    private static Stream<Arguments> provideEnumAndExpectedValues() {
        return Stream.of(
            Arguments.of(PostPublicationState.PUBLIC, "P", "전체 공개"),
            Arguments.of(PostPublicationState.ONLY_FOLLOWER, "F", "팔로워만"),
            Arguments.of(PostPublicationState.PRIVATE, "X", "비공개")
        );
    }

    @Test
    @DisplayName("모든 코드에 대해 열거형 변환 후 다시 코드로 변환했을 때 일치하는지 확인한다")
    void given_allCodes_when_convertBackAndForth_then_valuesAreConsistent() {
        for (PostPublicationState state : PostPublicationState.values()) {
            // given
            String originalCode = state.getCode();

            // when
            PostPublicationState convertedState = PostPublicationState.getStateFromCode(originalCode);
            String reconvertedCode = convertedState.getCode();

            // then
            assertEquals(originalCode, reconvertedCode);
            assertEquals(state, convertedState);
        }
    }
}