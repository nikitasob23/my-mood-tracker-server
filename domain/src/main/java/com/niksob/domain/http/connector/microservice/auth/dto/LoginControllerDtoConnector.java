package com.niksob.domain.http.connector.microservice.auth.dto;

import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import reactor.core.publisher.Mono;

public interface LoginControllerDtoConnector {
    Mono<Void> signup(SignupDetailsDto signupDetails);
}
