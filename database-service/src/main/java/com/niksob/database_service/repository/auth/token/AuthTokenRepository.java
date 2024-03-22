package com.niksob.database_service.repository.auth.token;

import com.niksob.database_service.entity.user.token.refresh.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long> {
}
