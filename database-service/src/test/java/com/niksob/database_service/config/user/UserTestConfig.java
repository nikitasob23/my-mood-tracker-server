package com.niksob.database_service.config.user;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestConfig {

    private static final String USERNAME = "TEST_USERNAME";
    private static final String NICKNAME = "TEST_NICKNAME";
    private static final String PASSWORD = "TEST_PASSWORD";

    @Bean
    public UsernameDto getUsernameDto() {
        return new UsernameDto(USERNAME);
    }

    @Bean
    public UserInfoDto getUserInfoDto() {
        return new UserInfoDto(USERNAME, NICKNAME, PASSWORD);
    }
}
