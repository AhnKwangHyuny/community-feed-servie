package org.faddy.user.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.faddy.User.application.interfaces.UserRepository;
import org.faddy.User.domain.User;

public class FakeUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            store.put(user.getId(), user);
            return user;
        }

        Long id = store.size() + 1L;
        User newUser = new User(id, user.getInfo());
        store.put(id, newUser);
        return newUser;
    }

    @Override
    public Optional<User> findById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("존재하지 않은 유저 id입니다.");
        }

        User user = store.get(userId);

        return Optional.ofNullable(user);
    }
}
