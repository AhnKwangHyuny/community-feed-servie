package org.fastcampus.community_feed.common.principal;

import org.fastcampus.community_feed.auth.domain.TokenProvider;
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
            if (authorization == null || authorization.split(" ").length != 2) {
                throw new IllegalArgumentException("Invalid token");
            }
            String token = authorization.split(" ")[1];

            Long userId = tokenProvider.getUserId(token);
            String role = tokenProvider.getRoles(token);

            return new UserPrincipal(userId, role);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
