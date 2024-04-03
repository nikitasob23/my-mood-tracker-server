package com.niksob.domain.http.connector.microservice.auth.login.dto;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import reactor.core.publisher.Mono;

public interface LoginControllerDtoConnector {
    Mono<Void> signup(SignupDetailsDto signupDetails);

    Mono<Void> signOut(SignOutDetailsDto signOutDetails);

    Mono<Void> signOutAll(UserIdDto userId);
}
