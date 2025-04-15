package org.fastcampus.community_feed.auth.repository;

import lombok.RequiredArgsConstructor;
import org.fastcampus.community_feed.auth.application.interfaces.UserAuthRepository;
import org.fastcampus.community_feed.auth.domain.UserAuth;
import org.fastcampus.community_feed.auth.repository.entity.UserAuthEntity;
import org.fastcampus.community_feed.auth.repository.jpa.JpaUserAuthRepository;
import org.fastcampus.community_feed.user.application.interfaces.UserRepository;
import org.fastcampus.community_feed.user.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserAuthRepositoryImpl implements UserAuthRepository {

    private final JpaUserAuthRepository jpaUserAuthRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void registerUser(UserAuth userAuth, User user) {
        User savedUser = userRepository.save(user);
        UserAuthEntity userAuthEntity = new UserAuthEntity(userAuth, savedUser.getId());
        jpaUserAuthRepository.save(userAuthEntity);
    }

    @Override
    public UserAuth findByEmail(String email) {
        UserAuthEntity userAuthEntity = jpaUserAuthRepository.findByEmail(email).orElseThrow();
        return userAuthEntity.toUserAuth();
    }
}
