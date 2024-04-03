package com.niksob.domain.http.connector.microservice.auth.login.dto;

import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import reactor.core.publisher.Mono;

public interface LoginControllerDtoConnector {
    Mono<Void> signup(SignupDetailsDto signupDetails);
}
