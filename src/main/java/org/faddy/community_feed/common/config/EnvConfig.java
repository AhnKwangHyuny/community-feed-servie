package org.faddy.community_feed.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * .env 파일에서 환경 변수를 로드하는 설정 클래스
 */
@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),
    @PropertySource(value = "classpath:application-${spring.profiles.active:default}.yml", ignoreResourceNotFound = true)
})
public class EnvConfig {

    @PostConstruct
    public void init() {
        try {
            // 프로젝트 루트 디렉토리의 .env 파일 경로
            File envFile = new File(".env");
            
            if (envFile.exists()) {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream(envFile);
                props.load(fis);
                fis.close();
                
                // 로드된 속성을 시스템 환경 변수로 설정
                for (String key : props.stringPropertyNames()) {
                    if (!System.getenv().containsKey(key)) {
                        String value = props.getProperty(key);
                        // '#' 으로 시작하는 라인은 주석이므로 무시
                        if (!key.trim().startsWith("#")) {
                            System.setProperty(key, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 로그만 출력하고 애플리케이션은 계속 실행
            System.err.println("Failed to load .env file: " + e.getMessage());
        }
    }
}
