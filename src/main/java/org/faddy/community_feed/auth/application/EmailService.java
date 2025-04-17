package org.faddy.community_feed.auth.application;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.auth.application.interfaces.EmailSendRepository;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationRepository;
import org.faddy.community_feed.auth.domain.Email;
import org.faddy.community_feed.auth.domain.RandomTokenGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailSendRepository emailSendRepository;
    private final EmailDomainService emailDomainService;
    
    /**
     * 이메일 전송 및 인증 처리
     * @param dto 이메일 전송 요청 DTO
     */
    public void sendEmail(SendEmailRequestDto dto) {
        // 이메일 객체 생성
        Email emailValue = Email.createEmail(dto.email());
        
        // 도메인 검증
        emailDomainService.isAllowedDomain(emailValue.getDomain());
        
        // 인증 토큰 생성 및 저장
        String randomToken = RandomTokenGenerator.generateToken();
        
        emailVerificationRepository.createEmailVerification(emailValue, randomToken);
        emailSendRepository.sendVerificationEmail(emailValue, randomToken);
    }
    
    /**
     * 이메일 검증
     * @param email 이메일 주소
     * @param token 인증 토큰
     */
    public void verify(String email, String token) {
        Email emailValue = Email.createEmail(email);
        emailVerificationRepository.verifyEmail(emailValue, token);
    }
}
