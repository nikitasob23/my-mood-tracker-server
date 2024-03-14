package com.niksob.database_service.dao.user.cached;

public interface UserEntityExistenceDao {
    boolean exists(Long userId);
}
