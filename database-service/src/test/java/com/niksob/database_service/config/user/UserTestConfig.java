package com.niksob.database_service.config.user;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.model.user.Nickname;
import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestConfig {

    @Bean("username")
    public String getUsernameStr() {
        return "TEST_USERNAME";
    }

    @Bean("nickname")
    public String getNicknameStr() {
        return "TEST_NICKNAME";
    }

    @Bean("password")
    public String getPasswordStr() {
        return "TEST_PASSWORD";
    }

    @Bean
    public Username getUsername() {
        return new Username(getUsernameStr());
    }

    @Bean
    public UsernameDto getUsernameDto() {
        return new UsernameDto(getUsernameStr());
    }

    @Bean
    public UserInfoDto getUserInfoDto() {
        return new UserInfoDto(getUsernameStr(), getNicknameStr(), getPasswordStr());
    }

    @Bean
    public UserInfo getUserInfo() {
        return new UserInfo(
                getUsername(),
                new Nickname(getNicknameStr()),
                new Password(getPasswordStr())
        );
    }
}
