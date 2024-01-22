package com.niksob.database_service.config.user;

import com.niksob.database_service.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@TestConfiguration
@ContextConfiguration(classes = {UserTestConfig.class})
public class UserEntityTestConfig {
    private static final long RANDOM_ID = 1;
    @Bean
    public UserEntity getUserEntity(
            @Qualifier("username") String username,
            @Qualifier("nickname") String nickname,
            @Qualifier("password") String password
    ) {
        return new UserEntity(RANDOM_ID, username, nickname, password);
    }
}
