package com.niksob.database_service.repository.user;

import com.niksob.database_service.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity getByUsername(String username);

    void deleteByUsername(String username);
}
