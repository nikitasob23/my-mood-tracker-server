package com.niksob.authorization_service.service.user;

import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Boolean> existsByEmailOrThrow(Email email);

    Mono<Boolean> existsByUsernameOrThrow(Username username);

    Mono<UserInfo> loadById(UserId userId);

    Mono<UserInfo> save(UserInfo userInfo);

    Mono<Void> update(UserInfo userInfo);
}
