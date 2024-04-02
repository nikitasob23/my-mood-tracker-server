package com.niksob.gateway_service.service.security;

import com.niksob.gateway_service.connector.security.database.user.UserSecurityDatabaseConnector;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserSecurityDetailsService implements ReactiveUserDetailsService {
    private final UserSecurityDatabaseConnector userSecurityConnector;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserSecurityDetailsService.class);

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userSecurityConnector.load(username)
                .map(userSecurityDetails -> (UserDetails) userSecurityDetails)
                .doOnNext(userSecurityDetails -> log.info(
                        "Load user security details from database microservice", null, userSecurityDetails
                )).doOnError(throwable -> log.error(
                        "Failure load user security details from database microservice", null, username
                ));
    }
}
