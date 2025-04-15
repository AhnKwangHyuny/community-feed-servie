package org.fastcampus.community_feed.acceptance.utils;

import static org.fastcampus.community_feed.acceptance.auth.LoginAcceptanceSteps.requestLoginGetToken;
import static org.fastcampus.community_feed.acceptance.post.UserAcceptanceSteps.followUser;

import org.fastcampus.community_feed.auth.application.dto.LoginRequestDto;
import org.fastcampus.community_feed.user.application.dto.FollowUserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTestTemplate {

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();

        followUser(new FollowUserRequestDto(1L, 2L));
        followUser(new FollowUserRequestDto(2L, 3L));
    }

    public void cleanUp() {
        databaseCleanup.execute();
    }

    protected String getEmailToken(String email) {
        return dataLoader.getEmailToken(email);
    }

    protected boolean isEmailVerified(String email) {
        return dataLoader.isEmailVerified(email);
    }

    protected Long getUserId(String email) {
        return dataLoader.getUserId(email);
    }

    protected String login(String email) {
        return requestLoginGetToken(new LoginRequestDto(email, "password"));
    }

    protected void createUser(String email) {
        dataLoader.createUser(email);
    }
}
