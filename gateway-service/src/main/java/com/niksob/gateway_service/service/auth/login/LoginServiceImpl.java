package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.http.connector.microservice.auth.login.LoginControllerConnector;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.user.UserId;
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
                .doOnError(throwable -> log.error("Failure signup", throwable, signupDetails));
    }

    @Override
    public Mono<Void> signOut(SignOutDetails signOutDetails) {
        return loginControllerConnector.signOut(signOutDetails)
                .doOnSuccess(ignore -> log.info("Successful sign out", null, signOutDetails))
                .doOnError(throwable -> log.error("Failure sign out", throwable, signOutDetails));
    }

    @Override
    public Mono<Void> signOutAll(UserId userId) {
        return loginControllerConnector.signOutAll(userId)
                .doOnSuccess(ignore -> log.info("Successful sign out from all devices", null, userId))
                .doOnError(throwable -> log.error("Failure sign out from all devices", throwable, userId));
    }
}
