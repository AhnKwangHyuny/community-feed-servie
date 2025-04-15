package org.fastcampus.community_feed.common.ui;

import org.fastcampus.community_feed.common.domain.exception.ErrorCode;

public record Response<T>(Integer code, String message, T value) {

    public static <T> Response<T> ok(T value) {
        return new Response<>(0, "ok", value);
    }
    public static <T> Response<T> error(ErrorCode code) {
        return new Response<>(code.getCode(), code.getMessage(), null);
    }
}
