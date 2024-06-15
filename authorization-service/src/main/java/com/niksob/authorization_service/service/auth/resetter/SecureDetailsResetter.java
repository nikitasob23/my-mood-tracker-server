package com.niksob.authorization_service.service.auth.resetter;

import com.niksob.domain.model.auth.login.UserEmail;
import com.niksob.domain.model.auth.login.UserPasswordPair;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import reactor.core.publisher.Mono;

public interface SecureDetailsResetter {
    Mono<Void> resetPassword(UserPasswordPair userPasswordPair);

    Mono<Void> resetEmail(UserEmail userEmail);

    Mono<Void> resetEmailByActiveCode(ActiveCode activeCode);
}
