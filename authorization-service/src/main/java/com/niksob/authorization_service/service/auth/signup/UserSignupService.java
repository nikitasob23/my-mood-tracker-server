package com.niksob.authorization_service.service.auth.signup;

import com.niksob.domain.model.auth.login.UserEmail;
import com.niksob.domain.model.auth.login.UserPasswordPair;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.user.UserId;
import reactor.core.publisher.Mono;

public interface UserSignupService {
    Mono<Void> signup(SignupDetails signupDetails);

    Mono<Void> resetPassword(UserPasswordPair userPasswordPair);

    Mono<Void> resetEmail(UserEmail userEmail);

    Mono<Void> signupByActiveCode(ActiveCode activeCode);

    Mono<Void> resetEmailByActiveCode(ActiveCode activeCode);

    Mono<Void> signOut(SignOutDetails signOutDetails);

    Mono<Void> signOutAll(UserId userId);
}
