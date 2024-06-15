package com.niksob.database_service.service.auth.token.loader;

import com.niksob.database_service.dao.auth.token.AuthTokenDao;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class AuthTokenLoaderServiceImpl implements AuthTokenLoaderService {
    protected final AuthTokenDao authTokenDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenLoaderServiceImpl.class);

    @Override
    public Mono<Boolean> existsByDetails(AuthTokenDetails authTokenDetails) {
        return MonoAsyncUtil.create(() -> authTokenDao.existsByDetails(authTokenDetails))
                .doOnNext(moodTags -> log.debug("Get auth token from DAO", moodTags))
                .doOnError(throwable -> log.error("Auth token existence check error", throwable, authTokenDetails));
    }

    @Override
    public Mono<EncodedAuthToken> load(AuthTokenDetails authTokenDetails) {
        return MonoAsyncUtil.create(() -> authTokenDao.load(authTokenDetails))
                .doOnNext(moodTags -> log.debug("Get auth token from DAO", moodTags))
                .doOnError(throwable -> log.error("Auth token loading error", throwable, authTokenDetails));
    }
}
