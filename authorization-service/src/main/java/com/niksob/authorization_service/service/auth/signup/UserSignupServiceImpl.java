package com.niksob.authorization_service.service.auth.signup;

import com.niksob.authorization_service.exception.auth.signup.DuplicateSignupAttemptException;
import com.niksob.authorization_service.exception.auth.signup.SignupException;
import com.niksob.authorization_service.mapper.auth.login.SignOutDetailsMapper;
import com.niksob.authorization_service.model.signup.SignupDetails;
import com.niksob.authorization_service.service.auth.token.AuthTokenService;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.authorization_service.values.user.DefaultUserInfo;
import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.http.connector.UserDatabaseConnector;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserSignupServiceImpl implements UserSignupService {
    private final AuthTokenService authTokenService;
    private final UserDatabaseConnector userDatabaseConnector;

    private final PasswordEncoderService passwordEncoderService;

    private final SignOutDetailsMapper signOutDetailsMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserSignupServiceImpl.class);

    @Override
    public Mono<Void> signup(SignupDetails signupDetails) {
        return Mono.just(signupDetails)
                .map(details -> new UserInfo(
                        signupDetails.getUsername(),
                        DefaultUserInfo.createNickname(signupDetails.getUsername()),
                        passwordEncoderService.encode(signupDetails.getRowPassword()))
                ).flatMap(userDatabaseConnector::save)
                .doOnSuccess(ignore -> log.info("Successful signup of user", null, signupDetails))
                .onErrorResume(throwable -> createSignupError(throwable, signupDetails));
    }

    @Override
    public Mono<Void> signOut(SignOutDetails signOutDetails) {
        return Mono.fromCallable(() -> signOutDetailsMapper.toAuthTokenDetails(signOutDetails))
                .flatMap(authTokenService::invalidate);
    }

    private Mono<Void> createSignupError(Throwable throwable, Object state) {
        Throwable e;
        if (throwable instanceof ResourceAlreadyExistsException) {
            e = new DuplicateSignupAttemptException("Duplicate signup attempt", throwable);
        } else if (throwable instanceof ResourceSavingException) {
            e = new SignupException(throwable.getMessage(), throwable);
        } else {
            e = throwable;
        }
        log.error("Signup failure", throwable, state);
        return Mono.error(e);
    }
}
