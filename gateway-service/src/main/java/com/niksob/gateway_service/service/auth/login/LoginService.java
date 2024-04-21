package com.niksob.gateway_service.service.auth.login;

import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.UserId;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<Void> signup(SignupDetails signupDetails);

    Mono<Void> signupByActiveCode(ActiveCode activeCode);

    Mono<Void> signOut(SignOutDetails signOutDetails);

    Mono<Void> signOutAll(UserId userId);
}
