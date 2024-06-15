package com.niksob.database_service.service.user.existence;

import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.Username;
import reactor.core.publisher.Mono;

public interface UserExistenceService {
    Mono<Boolean> existsByEmail(Email email);

    Mono<Boolean> existsByUsername(Username username);

    Mono<Boolean> existsById(UserId id);

    Mono<Boolean> existsOrThrow(UserId userId);
}
