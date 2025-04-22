package org.faddy.community_feed.auth.application;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.auth.application.dto.SendEmailVerificationTokenResponseDto;
import org.faddy.community_feed.auth.application.dto.VerifyEmailResponseDto;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationCacheRepository;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationRepository;
import org.faddy.community_feed.auth.domain.Email;
import org.faddy.community_feed.auth.infrastructure.email.EmailSender;
import org.faddy.community_feed.common.utils.EmailVerificationTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Duration TOKEN_EXPIRY = Duration.ofMinutes(3); // 3분 유효기간

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationCacheRepository emailVerificationCacheRepository;
    private final EmailSender emailSender;
    private final EmailDomainService emailDomainService;

    /**
     * 이메일 전송 및 인증 처리
     * @param dto 이메일 전송 요청 DTO
     * @return 발송 결과 정보
     */
    @Transactional
    public SendEmailVerificationTokenResponseDto sendEmail(SendEmailRequestDto dto) {
        try {
            // 이메일 객체 생성
            Email emailValue = Email.createEmail(dto.email());

            // 도메인 검증
            emailDomainService.isAllowedDomain(emailValue.getDomain());

            // 인증 토큰 생성 (6자리 숫자)
            String verificationToken = EmailVerificationTokenProvider.generate6DigitToken();

            // 토큰을 Redis에 저장 (3분 유효)
            emailVerificationCacheRepository.saveEmailVerificationToken(
                emailValue.toString(), verificationToken, TOKEN_EXPIRY);

            // 이메일 전송
            boolean sent = emailSender.sendVerificationEmail(emailValue, verificationToken);

            if (!sent) {
                // 이메일 전송 실패 시 Redis에서 토큰 삭제
                emailVerificationCacheRepository.removeEmailVerificationToken(emailValue.toString());
                throw new RuntimeException("이메일 전송에 실패했습니다.");
            }

            //이메일 인증 정보 저장
            emailVerificationRepository.createEmailVerification(emailValue , verificationToken);

            // 클라이언트에게 필요한 정보 반환
            return SendEmailVerificationTokenResponseDto.of(emailValue.getEmailText(), verificationToken, sent);
        } catch (Exception e) {
            log.error("이메일 전송 과정에서 오류 발생: {}", e.getMessage());
            return SendEmailVerificationTokenResponseDto.of(null, null, false);
        }
    }

    /**
     * 이메일 검증
     * @param email 이메일 주소
     * @param token 인증 토큰
     * @return 인증 결과 정보
     */
    @Transactional
    public VerifyEmailResponseDto verify(String email, String token) {
        Email emailValue = Email.createEmail(email);
        boolean isVerified = false;
        
        try {
            // Redis에서 토큰 검증
            isVerified = emailVerificationCacheRepository.verifyEmailToken(email, token);
            
            // 검증 결과와 관계없이 Redis에서 토큰 삭제 (중복 검증 방지)
            emailVerificationCacheRepository.removeEmailVerificationToken(email);
            
            if (!isVerified) {
                // 검증 실패 시 예외 던지지 않고 실패 응답 반환
                return VerifyEmailResponseDto.of(email, false, "유효하지 않거나 만료된 인증 코드입니다.");
            }
            
            // 인증 성공 시 DB에 인증 완료 상태 저장 (isVerify -> true)
            emailVerificationRepository.verifyEmail(emailValue, token);
            
            // 성공 응답 반환
            return VerifyEmailResponseDto.of(email, true, "이메일 인증이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            // 예외 발생 시 로깅
            System.err.println("이메일 인증 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            
            // 실패 응답 반환
            return VerifyEmailResponseDto.of(email, false, "이메일 인증 처리 중 오류가 발생했습니다.");
        }
    }

}