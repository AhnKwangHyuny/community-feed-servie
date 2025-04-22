package org.faddy.community_feed.common.response;

import org.faddy.community_feed.common.ui.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Response 타입이나 ApiResponse 타입이 아닌 응답만 처리
        return !(returnType.getParameterType().equals(Response.class) ||
                returnType.getParameterType().equals(ApiResponse.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        // 이미 ApiResponse로 래핑된 응답은 처리하지 않음
        if (body instanceof ApiResponse) {
            return body;
        }
        
        // 에러 응답인 경우 처리
        if (body instanceof Exception) {
            return ApiResponse.fail(((Exception) body).getMessage());
        }
        
        // String 타입은 특별 처리 필요
        if (body instanceof String) {
            // 문자열 반환의 경우 Handler에서 추가 처리가 필요할 수 있음
            // 여기서는 단순히 성공 메시지와 함께 반환
            return ApiResponse.success("Success", body);
        }
        
        // 일반적인 응답 처리
        return ApiResponse.success(body);
    }
} 