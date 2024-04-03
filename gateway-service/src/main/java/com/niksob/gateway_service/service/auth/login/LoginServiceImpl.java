package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.http.connector.microservice.auth.LoginControllerConnector;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginControllerConnector loginControllerConnector;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    public Mono<Void> signup(SignupDetails signupDetails) {
        return loginControllerConnector.signup(signupDetails)
                .doOnSuccess(ignore -> log.info("Successful signup of user", null, signupDetails))
                .doOnError(throwable -> log.error("Signup failure", throwable, signupDetails));
    }
}
