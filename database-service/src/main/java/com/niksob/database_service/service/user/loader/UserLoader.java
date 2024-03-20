package com.niksob.database_service.service.user.loader;

import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import reactor.core.publisher.Mono;

public interface UserLoader {
    Mono<UserInfo> loadById(UserId userId);

    Mono<UserInfo> loadAllById(UserId userId);

    Mono<UserInfo> loadByUsername(Username username);

    Mono<UserInfo> loadAllByUsername(Username username);
}
