package org.fastcampus.community_feed.common.config;

import java.util.List;
import org.fastcampus.community_feed.auth.domain.TokenProvider;
import org.fastcampus.community_feed.common.principal.AuthenticationPrincipalArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private TokenProvider tokenProvider;

    public AuthConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(tokenProvider));
    }
}
