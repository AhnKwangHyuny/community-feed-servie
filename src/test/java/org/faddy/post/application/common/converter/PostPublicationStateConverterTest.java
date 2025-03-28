package org.faddy.post.application.common.converter;

import org.faddy.post.domain.content.PostPublicationState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PostPublicationStateConverterTest {

    private final PostPublicationStateConverter converter = new PostPublicationStateConverter();

    @ParameterizedTest
    @MethodSource("provideStatesAndCodes")
    @DisplayName("열거형에서 데이터베이스 값으로 변환된다")
    void given_publicationState_when_convertToDatabaseColumn_then_returnsCorrectCode(
        PostPublicationState state, String expectedCode) {
        // when
        String actualCode = converter.convertToDatabaseColumn(state);
        // then
        assertEquals(expectedCode, actualCode);
    }

    @ParameterizedTest
    @MethodSource("provideCodesAndStates")
    @DisplayName("데이터베이스 값에서 열거형으로 변환된다")
    void given_databaseCode_when_convertToEntityAttribute_then_returnsCorrectState(
        String code, PostPublicationState expectedState) {
        // when
        PostPublicationState actualState = converter.convertToEntityAttribute(code);
        // then
        assertEquals(expectedState, actualState);
    }

    @Test
    @DisplayName("null 값을 데이터베이스 컬럼으로 변환하면 기본값을 반환한다")
    void given_nullState_when_convertToDatabaseColumn_then_returnsDefaultCode() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertEquals(PostPublicationState.PUBLIC.getCode(), result);
    }

    @Test
    @DisplayName("null 값을 엔티티 속성으로 변환하면 기본값을 반환한다")
    void given_nullCode_when_convertToEntityAttribute_then_returnsDefaultState() {
        // when
        PostPublicationState result = converter.convertToEntityAttribute(null);

        // then
        assertEquals(PostPublicationState.PUBLIC, result);
    }

    @Test
    @DisplayName("빈 문자열을 엔티티 속성으로 변환하면 기본값을 반환한다")
    void given_emptyString_when_convertToEntityAttribute_then_returnsDefaultState() {
        // when
        PostPublicationState result = converter.convertToEntityAttribute("");

        // then
        assertEquals(PostPublicationState.PUBLIC, result);
    }

    @Test
    @DisplayName("잘못된 코드를 변환하면 기본값을 반환한다")
    void given_invalidCode_when_convertToEntityAttribute_then_returnsDefaultState() {
        // given
        String invalidCode = "Z";

        // when
        PostPublicationState result = converter.convertToEntityAttribute(invalidCode);

        // then
        assertEquals(PostPublicationState.PUBLIC, result);
    }

    private static Stream<Arguments> provideStatesAndCodes() {
        return Stream.of(
            Arguments.of(PostPublicationState.PUBLIC, "P"),
            Arguments.of(PostPublicationState.ONLY_FOLLOWER, "F"),
            Arguments.of(PostPublicationState.PRIVATE, "X"),
            Arguments.of(null, "P") // null을 넣으면 기본값 "P"를 반환
        );
    }

    private static Stream<Arguments> provideCodesAndStates() {
        return Stream.of(
            Arguments.of("P", PostPublicationState.PUBLIC),
            Arguments.of("F", PostPublicationState.ONLY_FOLLOWER),
            Arguments.of("X", PostPublicationState.PRIVATE),
            Arguments.of(null, PostPublicationState.PUBLIC), // null을 넣으면 기본값 PUBLIC 반환
            Arguments.of("", PostPublicationState.PUBLIC)    // 빈 문자열을 넣으면 기본값 PUBLIC 반환
        );
    }
}