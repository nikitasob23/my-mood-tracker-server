package com.niksob.authorization_service.service.auth.signup;

import com.niksob.authorization_service.model.signup.SignupDetails;
import reactor.core.publisher.Mono;

public interface UserSignupService {
    Mono<Void> execute(SignupDetails signupDetails);
}
