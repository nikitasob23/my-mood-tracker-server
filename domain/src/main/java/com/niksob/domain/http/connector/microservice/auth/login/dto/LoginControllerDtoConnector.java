package com.niksob.domain.http.connector.microservice.auth.login.dto;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.auth.login.UserEmailDto;
import com.niksob.domain.dto.auth.login.UserPasswordPairDto;
import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import reactor.core.publisher.Mono;

public interface LoginControllerDtoConnector {
    Mono<Void> signup(SignupDetailsDto signupDetails);

    Mono<Void> resetEmail(UserEmailDto userEmail);

    Mono<Void> resetPassword(UserPasswordPairDto userPasswordPair);

    Mono<Void> signupByActiveCode(ActiveCodeDto activeCode);

    Mono<Void> resetEmailByActiveCode(ActiveCodeDto activeCode);

    Mono<Void> signOut(SignOutDetailsDto signOutDetails);

    Mono<Void> signOutAll(UserIdDto userId);
}
