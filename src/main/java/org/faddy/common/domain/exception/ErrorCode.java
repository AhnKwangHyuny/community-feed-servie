package org.faddy.common.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 400 BAD REQUEST
    INVALID_INPUT_VALUE(400, "입력값이 올바르지 않습니다"),
    INVALID_TYPE_VALUE(400, "타입이 올바르지 않습니다"),
    MISSING_REQUEST_PARAMETER(400, "필수 파라미터가 누락되었습니다"),
    METHOD_NOT_ALLOWED(405, "지원하지 않는 HTTP 메소드입니다"),
    UNSUPPORTED_MEDIA_TYPE(415, "지원하지 않는 미디어 타입입니다"),
    REQUEST_BODY_MISSING(400, "요청 바디가 누락되었습니다"),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(401, "인증이 필요합니다"),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(401, "만료된 토큰입니다"),

    // 403 FORBIDDEN
    ACCESS_DENIED(403, "접근 권한이 없습니다"),
    INVALID_PERMISSION(403, "권한이 올바르지 않습니다"),

    // 404 NOT FOUND
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다"),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다"),
    POST_NOT_FOUND(404, "게시글을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다"),
    RESOURCE_NOT_FOUND(404, "리소스를 찾을 수 없습니다"),

    // 409 CONFLICT
    DUPLICATE_RESOURCE(409, "이미 존재하는 리소스입니다"),
    USER_ALREADY_EXISTS(409, "이미 등록된 사용자입니다"),
    EMAIL_ALREADY_EXISTS(409, "이미 등록된 이메일입니다"),
    USERNAME_ALREADY_EXISTS(409, "이미 사용 중인 사용자명입니다"),

    // 429 TOO MANY REQUESTS
    TOO_MANY_REQUESTS(429, "요청이 너무 많습니다. 잠시 후 다시 시도해주세요"),

    // 500 서버 에러
    INTERNAL_ERROR(500, "서버 내부 오류가 발생했습니다"),
    DATABASE_ERROR(500, "데이터베이스 오류가 발생했습니다"),
    IO_ERROR(500, "입출력 오류가 발생했습니다"),
    EXTERNAL_SERVICE_ERROR(500, "외부 서비스 연동 중 오류가 발생했습니다"),

    // 503 서비스 이용불가
    SERVICE_UNAVAILABLE(503, "서비스를 일시적으로 이용할 수 없습니다");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // HTTP 상태 코드로 변환하는 유틸리티 메서드
    public int getStatus() {
        return code;
    }
}