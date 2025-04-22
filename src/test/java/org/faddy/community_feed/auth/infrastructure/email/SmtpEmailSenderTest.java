package org.faddy.community_feed.auth.infrastructure.email;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.faddy.community_feed.auth.domain.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SmtpEmailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private SmtpEmailSender emailSender;

    private final String testEmail = "test@example.com";
    private final String testToken = "123456";

    @Test
    void whenSendVerificationEmail_thenReturnsTrue() throws MessagingException {
        // given
        Email email = Email.createEmail(testEmail);
        ReflectionTestUtils.setField(emailSender, "baseUrl", "http://localhost:8080");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // when
        boolean result = emailSender.sendVerificationEmail(email, testToken);

        // then
        assertTrue(result);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void whenSendVerificationEmailWithException_thenReturnsFalse() throws MessagingException {
        // given
        Email email = Email.createEmail(testEmail);
        ReflectionTestUtils.setField(emailSender, "baseUrl", "http://localhost:8080");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MessagingException("Test exception")).when(mailSender).send(any(MimeMessage.class));

        // when
        boolean result = emailSender.sendVerificationEmail(email, testToken);

        // then
        assertFalse(result);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}