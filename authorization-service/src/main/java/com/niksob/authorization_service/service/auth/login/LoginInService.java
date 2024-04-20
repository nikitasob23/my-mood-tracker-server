package com.niksob.authorization_service.service.auth.login;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.user.SecurityUserDetails;
import reactor.core.publisher.Mono;

public interface LoginInService {
    Mono<SecurityUserDetails> loginInOrThrow(RowLoginInDetails rowLoginInDetails);
}
