package org.faddy.community_feed.common.principal;

import org.faddy.community_feed.auth.domain.TokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;

    public AuthenticationPrincipalArgumentResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            String authorization = webRequest.getHeader("Authorization");
            
            // 인증 헤더가 없거나 형식이 맞지 않으면 null 반환 (비로그인 사용자)
            if (authorization == null || authorization.split(" ").length != 2) {
                return null;
            }
            
            String token = authorization.split(" ")[1];
            
            try {
                // 토큰 처리 시도
                Long userId = tokenProvider.getUserId(token);
                String role = tokenProvider.getRoles(token);
                return new UserPrincipal(userId, role);
            } catch (Exception e) {
                // 토큰이 유효하지 않은 경우도 null 반환
                return null;
            }
        } catch (Exception e) {
            // 기타 예외 상황에서도 null 반환
            return null;
        }
    }
}
