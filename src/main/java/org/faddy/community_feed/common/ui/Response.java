package org.faddy.community_feed.common.ui;

import org.faddy.community_feed.common.domain.exception.ErrorCode;

public record Response<T>(Integer code, String message, T value) {

    // 성공 응답
    public static <T> Response<T> ok(T value) {
        return new Response<>(0, "ok", value);
    }

    // ErrorCode를 사용한 에러 응답
    public static <T> Response<T> error(ErrorCode code) {
        return new Response<>(code.getCode(), code.getMessage(), null);
    }

    // 문자열 메시지를 사용한 에러 응답
    public static <T> Response<T> error(String message) {
        return new Response<>(-1, message, null);
    }
}
