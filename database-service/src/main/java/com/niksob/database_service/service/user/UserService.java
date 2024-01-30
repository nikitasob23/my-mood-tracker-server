package com.niksob.database_service.service.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserInfo> load(Username username);

    Mono<Void> save(UserInfo userInfo);

    Mono<Void> update(UserInfo userInfo);

    Mono<Void> delete(Username username);
}
