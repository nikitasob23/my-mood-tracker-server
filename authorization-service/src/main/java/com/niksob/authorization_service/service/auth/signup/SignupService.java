package com.niksob.authorization_service.service.auth.signup;

import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import reactor.core.publisher.Mono;

public interface SignupService {
    Mono<Void> signup(SignupDetails signupDetails);

    Mono<Void> signupByActiveCode(ActiveCode activeCode);
}
