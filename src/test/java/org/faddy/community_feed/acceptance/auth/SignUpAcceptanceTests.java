package org.faddy.community_feed.acceptance.auth;

import org.faddy.community_feed.acceptance.utils.AcceptanceTestTemplate;
import org.junit.jupiter.api.BeforeEach;

public class SignUpAcceptanceTests extends AcceptanceTestTemplate {

    private final String email = "agh0314gmail.com";

    @BeforeEach
    void setup() {
        super.cleanUp();
    }


}
