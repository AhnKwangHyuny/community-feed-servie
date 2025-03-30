package org.faddy.User.infrastructure.repo;


import lombok.RequiredArgsConstructor;
import org.faddy.User.domain.User;
import org.faddy.User.infrastructure.repo.entity.UserRelationIdEntity;
import org.faddy.User.infrastructure.repo.jpa.JpaUserRelationRepository;
import org.faddy.User.infrastructure.repo.jpa.JpaUserRepository;
import org.faddy.post.application.repo.postQueue.UserPostQueueCommandRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRelationRepositoryImpl {
    private final JpaUserRepository jpaUserRepository;
    private final JpaUserRelationRepository jpaUserRelationRepository;

    public boolean isAlreadyFollow(User user, User targetUser) {
        UserRelationIdEntity id = new UserRelationIdEntity(user.getId(), targetUser.getId());
        return jpaUserRelationRepository.existsById(id);
    }
}
