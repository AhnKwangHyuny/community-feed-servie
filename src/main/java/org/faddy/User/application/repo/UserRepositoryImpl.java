package org.faddy.User.application.repo;

import org.faddy.User.application.interfae.UserRepository;
import org.faddy.User.domain.User;
import org.faddy.User.domain.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> userStore = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public User save(User user) {
        // 새로운 사용자인 경우 ID 생성
        if (user.getId() == null) {
            Long newId = sequence.incrementAndGet();
            User newUser = new User(newId, user.getInfo());
            userStore.put(newId, newUser);
            return newUser;
        }
        // 기존 사용자 업데이트
        else {
            userStore.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(userStore.get(userId));
    }
}