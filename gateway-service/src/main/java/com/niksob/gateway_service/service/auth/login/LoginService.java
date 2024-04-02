package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.model.auth.login.SignupDetails;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<Void> signup(SignupDetails signupDetails);
}