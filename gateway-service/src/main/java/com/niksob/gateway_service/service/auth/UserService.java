package com.niksob.gateway_service.service.auth;

import com.niksob.domain.model.user.User;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> loadByUsername(Username username);

    Mono<UserInfo> loadFullByUsername(Username username);

    Mono<UserInfo> save(UserInfo userInfo);

    Mono<Void> update(UserInfo userInfo);

    Mono<Void> delete(Username username);
}
