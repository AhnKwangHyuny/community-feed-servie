package org.faddy.community_feed.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * String 타입 반환 값을 처리하기 위한 특별한 ResponseBodyAdvice 구현
 * 
 * 문제: ResponseWrapper는 대부분의 타입을 처리할 수 있지만,
 * String 반환 타입은 특수한 경우로, 직접 JSON으로 변환해야 함
 */
@RestControllerAdvice
public class StringResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public StringResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // String 타입 반환 값만 처리
        return returnType.getParameterType().equals(String.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        // String이 아니면 처리하지 않음 (supports 메서드에서 이미 필터링되어야 함)
        if (!(body instanceof String)) {
            return body;
        }

        try {
            // String 응답을 ApiResponse로 래핑하고 JSON 문자열로 변환
            ApiResponse<Object> apiResponse = ApiResponse.success(body);
            // Content-Type을 application/json으로 설정
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            // ApiResponse 객체를 JSON 문자열로 직접 변환
            return objectMapper.writeValueAsString(apiResponse);
        } catch (Exception e) {
            // 변환 실패 시 원래 문자열 반환
            return body;
        }
    }
} 