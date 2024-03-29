package com.niksob.database_service.repository.auth.token;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long> {
    @Query("SELECT tokens FROM AuthTokenEntity tokens " +
            "WHERE tokens.userId = :#{#details.userId} AND tokens.device = :#{#details.device}")
    AuthTokenEntity getByDetails(AuthTokenEntityDetails details);

    Set<AuthTokenEntity> getAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
