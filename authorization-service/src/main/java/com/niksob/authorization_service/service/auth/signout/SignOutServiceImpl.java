package com.niksob.authorization_service.service.auth.signout;

import com.niksob.authorization_service.mapper.auth.login.SignOutDetailsMapper;
import com.niksob.authorization_service.service.auth.token.AuthTokenService;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SignOutServiceImpl implements SignOutService {
    private final AuthTokenService authTokenService;

    private final SignOutDetailsMapper signOutDetailsMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(SignOutServiceImpl.class);

    @Override
    public Mono<Void> signOut(SignOutDetails signOutDetails) {
        return Mono.fromCallable(() -> signOutDetailsMapper.toAuthTokenDetails(signOutDetails))
                .flatMap(authTokenService::invalidate)
                .doOnSuccess(ignore -> log.info("Successful sign out of user", null, signOutDetails))
                .doOnError(throwable -> log.error("Failure sign out of user", null, signOutDetails));
    }

    @Override
    public Mono<Void> signOutAll(UserId userId) {
        return authTokenService.invalidateByUserId(userId)
                .doOnSuccess(ignore -> log.info("Successful user sign out from all devices", null, userId))
                .doOnError(throwable -> log.error("Failure sign out of user", null, userId));
    }
}
