package org.faddy.community_feed.common.idempotency;

import lombok.Getter;
import org.faddy.community_feed.common.ui.Response;

@Getter
public class Idempotency {
    private final String key;
    private final Response<?> response;

    public Idempotency(String key, Response<?> response) {
        this.key = key;
        this.response = response;
    }
}
