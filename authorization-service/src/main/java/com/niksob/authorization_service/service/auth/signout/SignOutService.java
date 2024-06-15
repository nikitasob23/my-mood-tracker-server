package com.niksob.authorization_service.service.auth.signout;

import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.user.UserId;
import reactor.core.publisher.Mono;

public interface SignOutService {
    Mono<Void> signOut(SignOutDetails signOutDetails);

    Mono<Void> signOutAll(UserId userId);
}
