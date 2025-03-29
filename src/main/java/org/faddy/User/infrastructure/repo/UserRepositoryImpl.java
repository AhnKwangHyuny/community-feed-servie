package org.faddy.User.infrastructure.repo;

import lombok.RequiredArgsConstructor;
import org.faddy.User.application.interfaces.UserRepository;
import org.faddy.User.infrastructure.repo.entity.UserEntity;
import org.faddy.User.infrastructure.repo.jpa.JpaUserRepository;
import org.faddy.User.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.fromUser(user);
        UserEntity savedUserEntity = jpaUserRepository.save(userEntity);

        return savedUserEntity.toUser();
    }

    @Override
    public Optional<User> findById(Long userId) {
        UserEntity findedUserEntity = jpaUserRepository.findById(userId)
            .orElseThrow(IllegalArgumentException::new);

        User findedUser = findedUserEntity.toUser();
        return Optional.ofNullable(findedUser);
    }
}