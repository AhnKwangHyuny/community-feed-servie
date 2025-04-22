package org.faddy.community_feed.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ResponseWrapperConfig {

    /**
     * String 반환 타입을 처리하기 위한 특별한 HttpMessageConverter 설정
     * 
     * 문제: Spring MVC에서 컨트롤러가 String을 반환할 때, ResponseBodyAdvice를 통해 래핑된 객체는
     * MappingJackson2HttpMessageConverter를 사용하여 변환되어야 하지만, 
     * 기본적으로 String 반환 값은 StringHttpMessageConverter를 사용함
     * 
     * 해결: ObjectMapper를 사용하여 모든 타입을 처리할 수 있는 MappingJackson2HttpMessageConverter를 Bean으로 등록
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        // 모든 미디어 타입에 대해 변환기 적용 가능
        converter.setSupportedMediaTypes(java.util.Collections.singletonList(org.springframework.http.MediaType.ALL));
        return converter;
    }
} 