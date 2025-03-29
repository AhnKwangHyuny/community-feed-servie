package org.faddy.User.infrastructure.repo.jpa;

import org.faddy.User.infrastructure.repo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

}
