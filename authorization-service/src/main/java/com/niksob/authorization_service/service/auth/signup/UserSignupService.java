package com.niksob.authorization_service.service.auth.signup;

import com.niksob.authorization_service.model.signup.SignupDetails;
import com.niksob.domain.model.auth.login.SignOutDetails;
import reactor.core.publisher.Mono;

public interface UserSignupService {
    Mono<Void> signup(SignupDetails signupDetails);

    Mono<Void> signOut(SignOutDetails signOutDetails);
}
