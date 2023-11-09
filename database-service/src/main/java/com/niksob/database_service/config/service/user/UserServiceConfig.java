package com.niksob.database_service.config.service.user;

import com.niksob.database_service.service.user.UserService;
import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.model.user.Nickname;
import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

@Configuration
@AllArgsConstructor
public class UserServiceConfig {

    @Bean
    public UserService getUserService() {
        return new UserService() {

            private final Logger log = LoggerFactory.getLogger(UserService.class);

            @Override
            public UserInfo load(Username username) {
                // throws an exception in case of a wrong test password
                if (Stream.of(username)
                        .map(Username::getValue)
                        .map(String::toUpperCase)
                        .anyMatch(u -> u.contains("THROW"))) {
                    final IllegalUserAccessException e = new IllegalUserAccessException(username, "User is trying to access someone else's data");
                    log.debug("Failed loading of user info", e);
                    throw e;
                }
                return new UserInfo()
                        .setUsername(new Username("TEST_USERNAME"))
                        .setNickname(new Nickname("TEST_NICKNAME"))
                        .setPassword(new Password("TEST_PASSWORD"));
            }

            @Override
            public void save(UserInfo userInfo) {
                if (userInfo.getPassword()
                        .getValue()
                        .equals("0000")) {
                    final IllegalArgumentException e = new IllegalArgumentException("Unsafe password");
                    log.debug("Failed saving user", e, userInfo);
                    throw e;
                }
            }
        };
    }
}
