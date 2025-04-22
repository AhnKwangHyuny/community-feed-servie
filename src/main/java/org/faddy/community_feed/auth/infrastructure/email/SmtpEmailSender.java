package org.faddy.community_feed.auth.infrastructure.email;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.auth.domain.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public SmtpEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendVerificationEmail(Email email, String verificationToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email.getEmailText());
            helper.setSubject("이메일 인증을 완료해주세요");

            String verificationUrl = baseUrl + "/signup/verify-email?email=" + email.getEmailText() + "&token=" + verificationToken;

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                + "<h2>이메일 인증</h2>"
                + "<p>안녕하세요! 가입해 주셔서 감사합니다.</p>"
                + "<p>아래 인증 코드를 입력하여 이메일 인증을 완료해주세요:</p>"
                + "<div style='margin: 25px 0;'>"
                + "<h3 style='font-size: 24px; letter-spacing: 5px; text-align: center;'>" + verificationToken + "</h3>"
                + "</div>"
                + "<p>인증 코드는 3분 동안 유효합니다.</p>"
                + "<p>감사합니다!</p>"
                + "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", email, e.getMessage());
            return false;
        }
    }
}