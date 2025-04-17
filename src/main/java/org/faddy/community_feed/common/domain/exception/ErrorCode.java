package org.faddy.community_feed.common.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "invalid input value"),
    NOT_FOUND(404, "not found value"),
    INTERNAL_ERROR(500, "unexpected error"),
    INVALID_EMAIL_ADDRESS(400, "email address is invalid"),
    DUPLICATE_VERIFICATION_TOKEN(400, "duplicate verification token"),
    ;

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
