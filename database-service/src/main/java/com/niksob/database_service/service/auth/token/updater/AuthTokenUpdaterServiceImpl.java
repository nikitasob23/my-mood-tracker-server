package com.niksob.database_service.service.auth.token.updater;

import com.niksob.database_service.dao.auth.token.AuthTokenDao;
import com.niksob.database_service.service.auth.token.loader.AuthTokenLoaderServiceImpl;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthTokenMapper;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthTokenUpdaterServiceImpl extends AuthTokenLoaderServiceImpl implements AuthTokenUpdaterService {
    private final UserExistenceService userExistenceService;
    private final EncodedAuthTokenMapper authTokenMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenUpdaterServiceImpl.class);

    public AuthTokenUpdaterServiceImpl(
            AuthTokenDao authTokenDao, UserExistenceService userExistenceService, EncodedAuthTokenMapper authTokenMapper
    ) {
        super(authTokenDao);
        this.userExistenceService = userExistenceService;
        this.authTokenMapper = authTokenMapper;
    }

    @Override
    public Mono<EncodedAuthToken> save(EncodedAuthToken authToken) {
        return userExistenceService.existsOrThrow(authToken.getUserId())
                .flatMap(userExists -> MonoAsyncUtil.create(() -> authTokenDao.save(authToken)))
                .doOnSuccess(ignore -> log.info("Auth token is saved", null, authToken))
                .doOnError(throwable -> log.error("Auth token saving error", throwable, authTokenDao));
    }

    @Override
    public Mono<EncodedAuthToken> update(EncodedAuthToken authToken) {
        return setIdIfNull(authToken).flatMap(token ->
                MonoAsyncUtil.create(() -> authTokenDao.update(token))
                        .doOnNext(t -> log.debug("Update auth token in DAO", t))
                        .doOnError(throwable -> log.error("Auth token updating error", throwable, authToken)));
    }

    @Override
    public Mono<Void> delete(AuthTokenDetails authTokenDetails) {
        return MonoAsyncUtil.create(() -> authTokenDao.delete(authTokenDetails))
                .doOnSuccess(ignore -> log.debug("Delete auth token from DAO", authTokenDetails))
                .doOnError(throwable -> log.error("Auth token deleting error", throwable, authTokenDetails));
    }

    @Override
    public Mono<Void> deleteByUserId(UserId userId) {
        return MonoAsyncUtil.create(() -> authTokenDao.deleteByUserId(userId))
                .doOnSuccess(ignore -> log.debug("Delete all user's auth tokens from DAO", userId))
                .doOnError(throwable -> log.error("All user's auth token deleting error", throwable, userId));
    }

    private Mono<EncodedAuthToken> setIdIfNull(EncodedAuthToken authToken) {
        return Mono.just(authToken)
                .filter(token -> token.getId() != null)
                .switchIfEmpty(Mono.fromCallable(() -> authTokenMapper.getDetails(authToken))
                        .flatMap(this::load)
                        .map(tokenWithId -> authTokenMapper.combine(tokenWithId, authToken)));
    }
}
