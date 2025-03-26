package org.faddy.User.application.interfae;

import java.util.Optional;
import org.faddy.User.domain.User;

public interface UserRepository {

    // 유저 저장
    User save(User user);
    Optional<User> findById(Long userId);
    // create User wrapped Optional Object

}
