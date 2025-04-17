package org.faddy.community_feed.auth.application;

import org.faddy.community_feed.auth.application.dto.CreateUserAuthRequestDto;
import org.faddy.community_feed.auth.application.dto.LoginRequestDto;
import org.faddy.community_feed.auth.application.dto.UserAccessTokenResponseDto;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationRepository;
import org.faddy.community_feed.auth.application.interfaces.UserAuthRepository;
import org.faddy.community_feed.auth.domain.Email;
import org.faddy.community_feed.auth.domain.TokenProvider;
import org.faddy.community_feed.auth.domain.UserAuth;
import org.faddy.community_feed.user.domain.User;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 서비스
 */
@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserAuthRepository userAuthRepository;
    private final EmailDomainService emailDomainService;

    public AuthService(
            TokenProvider tokenProvider,
            EmailVerificationRepository emailVerificationRepository,
            UserAuthRepository userAuthRepository,
            EmailDomainService emailDomainService
    ) {
        this.tokenProvider = tokenProvider;
        this.emailVerificationRepository = emailVerificationRepository;
        this.userAuthRepository = userAuthRepository;
        this.emailDomainService = emailDomainService;
    }

    /**
     * 사용자 등록
     */
    public UserAccessTokenResponseDto registerUser(CreateUserAuthRequestDto dto) {
        // 이메일 생성
        Email email = Email.createEmail(dto.email());
        
        // 도메인 검증
        emailDomainService.isAllowedDomain(email.getDomain());

        if (!emailVerificationRepository.isEmailVerified(email)) {
            throw new IllegalArgumentException("Email is not verified");
        }

        UserAuth userAuth = new UserAuth(dto.email(), dto.password(), dto.role());
        User user = new User(dto.name(), dto.profileImageUrl());
        Long savedUserId = userAuthRepository.registerUser(userAuth, user);

        //등록된 user id userAuth에 추가
        userAuth.setUserId(savedUserId);

        return createToken(userAuth);
    }

    /**
     * 사용자 로그인
     */
    public UserAccessTokenResponseDto loginUser(LoginRequestDto dto) {
        UserAuth userAuth = userAuthRepository.findByEmail(dto.email());

        if (!userAuth.matchPassword(dto.password())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return createToken(userAuth);
    }

    /**
    * 사용자 인증 성공 토큰 발급
    */

    private UserAccessTokenResponseDto createToken(UserAuth userAuth) {
        String token = tokenProvider.createToken(userAuth.getUserId(), userAuth.getRole());
        return new UserAccessTokenResponseDto(token);
    }
}
