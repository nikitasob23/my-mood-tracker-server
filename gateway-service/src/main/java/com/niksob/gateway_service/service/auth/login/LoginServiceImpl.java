package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.http.connector.microservice.auth.LoginControllerConnector;
import com.niksob.domain.model.auth.login.SignupDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginControllerConnector loginControllerConnector;

    @Override
    public Mono<Void> signup(SignupDetails signupDetails) {
        return loginControllerConnector.signup(signupDetails);
//                .doOnSuccess(ignore -> )
//                .onErrorResume(throwable -> );
    }
}
