package org.faddy.community_feed.auth.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

/**
 * 이메일 도메인 검증을 위한 서비스
 */
@Service
public class EmailDomainService {
    
    private final Set<String> allowedDomains = new HashSet<>();
    
    @Value("${email.allowed-domains:naver.com,gmail.com}")
    private String[] defaultAllowedDomains;
    
    @PostConstruct
    public void init() {
        allowedDomains.addAll(Arrays.asList(defaultAllowedDomains));
    }
    
    /**
     * 도메인이 허용되는지 확인하고, 허용되지 않으면 예외 발생
     * @param domain 확인할 도메인
     * @throws IllegalArgumentException 허용되지 않는 도메인인 경우
     */
    public void isAllowedDomain(String domain) {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain must not be empty");
        }
        
        if (!allowedDomains.contains(domain.toLowerCase())) {
            throw new IllegalArgumentException(
                "Domain is not allowed. Currently supported domains: " + 
                String.join(", ", getAllowedDomains())
            );
        }
    }
    
    /**
     * 현재 허용된 모든 도메인 목록 반환
     * @return 허용 도메인 목록
     */
    public Set<String> getAllowedDomains() {
        return Collections.unmodifiableSet(allowedDomains);
    }
    
    /**
     * 허용 도메인 추가
     */
    public void addAllowedDomain(String domain) {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain must not be empty");
        }
        allowedDomains.add(domain.toLowerCase());
    }


    
    /**
     * 여러 허용 도메인 추가
     */
    public void addAllowedDomains(List<String> domains) {
        if (domains == null) {
            throw new IllegalArgumentException("Domains list must not be null");
        }
        domains.forEach(domain -> allowedDomains.add(domain.toLowerCase()));
    }
}
