package com.niksob.database_service.service.auth.token;

import com.niksob.database_service.dao.auth.token.AuthTokenDao;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.auth.token.UserAuthToken;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final AuthTokenDao authTokenDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<UserAuthToken> save(UserAuthToken authToken) {
        return MonoAsyncUtil.create(() -> authTokenDao.save(authToken))
                .doOnSuccess(ignore -> log.info("Auth token is saved", null, authToken))
                .doOnError(throwable -> log.error("Auth token saving error", throwable, authTokenDao));
    }
}
