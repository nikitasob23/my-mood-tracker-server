package com.niksob.database_service.dao.user.existence;

public interface UserEntityExistenceDao {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(Long id);
}
