package com.niksob.database_service.dao.user.existence;

public interface UserEntityExistenceDao {
    boolean existsByUsername(String username);

    boolean existsById(Long id);
}
