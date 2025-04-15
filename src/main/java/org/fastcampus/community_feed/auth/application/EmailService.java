package org.fastcampus.community_feed.auth.application;

import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.auth.application.dto.SendEmailRequestDto;
import org.fastcampus.community_feed.auth.application.interfaces.EmailSendRepository;
import org.fastcampus.community_feed.auth.application.interfaces.EmailVerificationRepository;
import org.fastcampus.community_feed.auth.domain.Email;
import org.fastcampus.community_feed.auth.domain.RandomTokenGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailSendRepository emailSendRepository;

    public void sendEmail(SendEmailRequestDto dto) {
        Email emailValue = Email.createEmail(dto.email());
        String randomToken = RandomTokenGenerator.generateToken();

        emailVerificationRepository.createEmailVerification(emailValue, randomToken);
        emailSendRepository.sendVerificationEmail(emailValue, randomToken);
    }

    public void verify(String email, String token) {
        Email emailValue = Email.createEmail(email);
        emailVerificationRepository.verifyEmail(emailValue, token);
    }
}
