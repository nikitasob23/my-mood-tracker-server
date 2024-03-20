package com.niksob.database_service.config.dao.user.existence;

import com.niksob.database_service.dao.user.existence.UserEntityExistenceDao;
import com.niksob.database_service.dao.user.existence.UserEntityExistenceDaoImpl;
import com.niksob.database_service.dao.user.loader.UserEntityLoaderDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UserEntityExistenceDaoConfig {
    @Bean("userEntityExistenceDao")
    public UserEntityExistenceDao getUserEntityExistenceDao(@Lazy UserEntityLoaderDao loaderDao) {
        return new UserEntityExistenceDaoImpl(loaderDao);
    }
}
