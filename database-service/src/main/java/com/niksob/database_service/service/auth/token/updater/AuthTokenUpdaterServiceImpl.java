package com.niksob.database_service.service.auth.token.updater;

import com.niksob.database_service.dao.auth.token.AuthTokenDao;
import com.niksob.database_service.service.auth.token.loader.AuthTokenLoaderServiceImpl;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthTokenMapper;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthTokenUpdaterServiceImpl extends AuthTokenLoaderServiceImpl implements AuthTokenUpdaterService {
    private final EncodedAuthTokenMapper authTokenMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenUpdaterServiceImpl.class);

    public AuthTokenUpdaterServiceImpl(AuthTokenDao authTokenDao, EncodedAuthTokenMapper authTokenMapper) {
        super(authTokenDao);
        this.authTokenMapper = authTokenMapper;
    }

    @Override
    public Mono<EncodedAuthToken> save(EncodedAuthToken authToken) {
        return MonoAsyncUtil.create(() -> authTokenDao.save(authToken))
                .doOnSuccess(ignore -> log.info("Auth token is saved", null, authToken))
                .doOnError(throwable -> log.error("Auth token saving error", throwable, authTokenDao));
    }

    @Override
    public Mono<Void> update(EncodedAuthToken authToken) {
        return setIdIfNull(authToken).flatMap(token ->
                MonoAsyncUtil.create(() -> authTokenDao.update(token))
                        .doOnSuccess(ignore -> log.debug("Update auth token in DAO", authToken))
                        .doOnError(throwable -> log.error("Auth token updating error", throwable, authToken)));
    }

    @Override
    public Mono<Void> delete(AuthTokenDetails authTokenDetails) {
        return MonoAsyncUtil.create(() -> authTokenDao.delete(authTokenDetails))
                .doOnSuccess(ignore -> log.debug("Delete auth token from DAO", authTokenDetails))
                .doOnError(throwable -> log.error("Auth token deleting error", throwable, authTokenDetails));
    }

    private Mono<EncodedAuthToken> setIdIfNull(EncodedAuthToken authToken) {
        return Mono.just(authToken)
                .filter(token -> token.getId() != null)
                .switchIfEmpty(Mono.fromCallable(() -> authTokenMapper.getDetails(authToken))
                        .flatMap(this::load)
                        .map(tokenWithId -> authTokenMapper.combine(tokenWithId, authToken)));
    }
}
