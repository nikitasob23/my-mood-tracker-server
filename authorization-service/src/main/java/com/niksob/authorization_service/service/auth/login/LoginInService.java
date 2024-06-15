package com.niksob.authorization_service.service.auth.login;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.user.User;
import reactor.core.publisher.Mono;

public interface LoginInService {
    Mono<User> loginInOrThrow(RowLoginInDetails rowLoginInDetails);
}
