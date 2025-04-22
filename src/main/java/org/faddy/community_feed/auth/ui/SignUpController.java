package org.faddy.community_feed.auth.ui;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.auth.application.AuthService;
import org.faddy.community_feed.auth.application.EmailService;
import org.faddy.community_feed.auth.application.dto.CreateUserAuthRequestDto;
import org.faddy.community_feed.auth.application.dto.SendEmailRequestDto;
import org.faddy.community_feed.auth.application.dto.UserAccessTokenResponseDto;
import org.faddy.community_feed.auth.application.dto.VerifyEmailRequestDto;
import org.faddy.community_feed.auth.application.dto.VerifyEmailResponseDto;
import org.faddy.community_feed.common.response.ApiResponse;
import org.faddy.community_feed.common.ui.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final EmailService emailService;
    private final AuthService authService;

    @PostMapping("/send-verification-email")
    public Response<Void> sendEmail(@RequestBody SendEmailRequestDto dto) {
        emailService.sendEmail(dto);
        return Response.ok(null);
    }

    @PostMapping("/verify-email")
    public ApiResponse<VerifyEmailResponseDto> verifyEmail(@RequestBody VerifyEmailRequestDto dto) {

        VerifyEmailResponseDto response = emailService.verify(dto.getEmail(), dto.getToken());
        boolean isSucessed = response.verified();

        if(isSucessed) {
            return ApiResponse.success(response);

        }

        return ApiResponse.fail("이메일 인증에 실패했습니다. 다시 시도해 주시길 바랍니다.");
    }

    @PostMapping("/register")
    public Response<UserAccessTokenResponseDto> register(@RequestBody CreateUserAuthRequestDto dto) {
        return Response.ok(authService.registerUser(dto));
    }
}