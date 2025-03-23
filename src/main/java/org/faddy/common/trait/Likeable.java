package org.faddy.common.trait;

import org.faddy.User.domain.User;

public interface Likeable {
    void like(User user);
    void unlike(User user);
    int getLikeCount();

    // 공통 검증 로직 (자신의 comment , post 에는 좋아요 금지)
    void validateLikeOperation(User user);
}