package com.niksob.database_service.repository.auth.token;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long> {
    Set<AuthTokenEntity> getAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
