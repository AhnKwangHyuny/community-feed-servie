package org.faddy.community_feed.auth.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.auth.application.dto.SendEmailVerificationTokenResponseDto;
import org.faddy.community_feed.auth.application.dto.VerifyEmailResponseDto;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationCacheRepository;
import org.faddy.community_feed.auth.application.interfaces.EmailVerificationRepository;
import org.faddy.community_feed.auth.domain.Email;
import org.faddy.community_feed.auth.infrastructure.email.EmailSender;
import org.faddy.community_feed.common.utils.EmailVerificationTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private EmailVerificationCacheRepository emailVerificationCacheRepository;

    @Mock
    private EmailSender emailSender;

    @Mock
    private EmailDomainService emailDomainService;

    @InjectMocks
    private EmailService emailService;

    private final String testEmail = "test@naver.com";
    private final String testToken = "123456";
    private final Duration tokenExpiry = Duration.ofMinutes(3);

    @BeforeEach
    void setUp() {
        doNothing().when(emailDomainService).isAllowedDomain(anyString());
    }

    @Test
    void whenSendEmail_thenSavesToCacheAndSendsEmail() {
        // given
        SendEmailRequestDto dto = new SendEmailRequestDto(testEmail);

        try (MockedStatic<EmailVerificationTokenProvider> mockedStatic = Mockito.mockStatic(EmailVerificationTokenProvider.class)) {
            mockedStatic.when(EmailVerificationTokenProvider::generate6DigitToken).thenReturn(testToken);

            doNothing().when(emailVerificationCacheRepository).saveEmailVerificationToken(anyString(), anyString(), any(Duration.class));
            when(emailSender.sendVerificationEmail(any(Email.class), anyString())).thenReturn(true);

            // when
            SendEmailVerificationTokenResponseDto response = emailService.sendEmail(dto);

            // then
            assertNotNull(response);
            assertEquals(testEmail, response.email());
            assertTrue(response.sent());

            verify(emailVerificationCacheRepository, times(1))
                .saveEmailVerificationToken(eq(testEmail), eq(testToken), any(Duration.class));
            verify(emailSender, times(1))
                .sendVerificationEmail(any(Email.class), eq(testToken));
        }
    }

    @Test
    void whenSendEmailAndEmailSendingFails_thenRemovesFromCacheAndReturnsFalse() {
        // given
        SendEmailRequestDto dto = new SendEmailRequestDto(testEmail);

        try (MockedStatic<EmailVerificationTokenProvider> mockedStatic = Mockito.mockStatic(EmailVerificationTokenProvider.class)) {
            mockedStatic.when(EmailVerificationTokenProvider::generate6DigitToken).thenReturn(testToken);

            doNothing().when(emailVerificationCacheRepository).saveEmailVerificationToken(anyString(), anyString(), any(Duration.class));
            when(emailSender.sendVerificationEmail(any(Email.class), anyString())).thenReturn(false);
            doNothing().when(emailVerificationCacheRepository).removeEmailVerificationToken(anyString());

            // when
            SendEmailVerificationTokenResponseDto response = emailService.sendEmail(dto);

            // then
            assertNotNull(response);
            assertFalse(response.sent());

            verify(emailVerificationCacheRepository, times(1))
                .saveEmailVerificationToken(eq(testEmail), eq(testToken), any(Duration.class));
            verify(emailVerificationCacheRepository, times(1))
                .removeEmailVerificationToken(eq(testEmail));
        }
    }

    @Test
    void whenVerifyEmailWithValidToken_thenVerifiesAndRemovesFromCache() {
        // given
        when(emailVerificationCacheRepository.verifyEmailToken(testEmail, testToken)).thenReturn(true);
        doNothing().when(emailVerificationCacheRepository).removeEmailVerificationToken(testEmail);
        doNothing().when(emailVerificationRepository).verifyEmail(any(Email.class), eq(testToken));

        // when
        VerifyEmailResponseDto response = emailService.verify(testEmail, testToken);

        // then
        assertNotNull(response);
        assertEquals(testEmail, response.email());
        assertTrue(response.verified());

        verify(emailVerificationCacheRepository, times(1)).verifyEmailToken(testEmail, testToken);
        verify(emailVerificationCacheRepository, times(1)).removeEmailVerificationToken(testEmail);
        verify(emailVerificationRepository, times(1)).verifyEmail(any(Email.class), eq(testToken));
    }

    @Test
    void whenVerifyEmailWithInvalidToken_thenThrowsException() {
        // given
        when(emailVerificationCacheRepository.verifyEmailToken(testEmail, testToken)).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> emailService.verify(testEmail, testToken));

        verify(emailVerificationCacheRepository, times(1)).verifyEmailToken(testEmail, testToken);
        verify(emailVerificationCacheRepository, times(0)).removeEmailVerificationToken(anyString());
        verify(emailVerificationRepository, times(0)).verifyEmail(any(Email.class), anyString());
    }
}