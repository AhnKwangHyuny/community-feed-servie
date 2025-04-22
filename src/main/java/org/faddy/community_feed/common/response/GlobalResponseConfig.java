package org.faddy.community_feed.common.response;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * 모든 컨트롤러 응답을 일관된 형식으로 처리하기 위한 전역 설정 클래스
 */
@Configuration
public class GlobalResponseConfig {

    /**
     * ResponseWrapper와 StringResponseHandler가 모든 컨트롤러에 
     * 올바른 순서로 적용되도록 로그 메시지 추가
     */
    public GlobalResponseConfig() {
        System.out.println("GlobalResponseConfig 초기화: 통합 API 응답 포맷 활성화");
        System.out.println("- ResponseWrapper: 일반 객체 응답 처리");
        System.out.println("- StringResponseHandler: String 타입 응답 특별 처리");
    }
} 