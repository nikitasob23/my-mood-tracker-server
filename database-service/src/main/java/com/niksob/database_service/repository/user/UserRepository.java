package com.niksob.database_service.repository.user;

import com.niksob.database_service.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    UserEntity getByUsername(String username);

    @Modifying
    @Query("UPDATE UserEntity u " +
            "SET u.username = :#{#user.username}, u.nickname = :#{#user.nickname}, u.password = :#{#user.password} " +
            "WHERE u.id = :#{#user.id}"
    )
    void update(UserEntity user);

    void deleteByUsername(String username);
}
