package com.niksob.database_service.service.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserInfo> load(Username username);

    Mono<UserInfo> save(UserInfo userInfo);

    Mono<UserInfo> update(UserInfo userInfo);

    Mono<UserInfo> delete(Username username);
}
