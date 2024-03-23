package com.niksob.database_service.service.auth.token.updater;

import com.niksob.database_service.dao.auth.token.AuthTokenDao;
import com.niksob.database_service.service.auth.token.loader.AuthTokenLoaderServiceImpl;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthTokenUpdaterServiceImpl extends AuthTokenLoaderServiceImpl implements AuthTokenUpdaterService {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenUpdaterServiceImpl.class);

    public AuthTokenUpdaterServiceImpl(AuthTokenDao authTokenDao) {
        super(authTokenDao);
    }

    @Override
    public Mono<EncodedAuthToken> save(EncodedAuthToken authToken) {
        return MonoAsyncUtil.create(() -> authTokenDao.save(authToken))
                .doOnSuccess(ignore -> log.info("Auth token is saved", null, authToken))
                .doOnError(throwable -> log.error("Auth token saving error", throwable, authTokenDao));
    }

    @Override
    public Mono<Void> update(EncodedAuthToken authToken) {
        return MonoAsyncUtil.create(() -> authTokenDao.update(authToken))
                .doOnSuccess(ignore -> log.debug("Update auth token in DAO", authToken))
                .doOnError(throwable -> log.error("Auth token updating error", throwable, authToken));
    }

    @Override
    public Mono<Void> delete(AuthTokenDetails authTokenDetails) {
        return MonoAsyncUtil.create(() -> authTokenDao.delete(authTokenDetails))
                .doOnSuccess(ignore -> log.debug("Delete auth token from DAO", authTokenDetails))
                .doOnError(throwable -> log.error("Auth token deleting error", throwable, authTokenDetails));
    }
}
