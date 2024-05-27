package com.niksob.authorization_service.service.auth.signup;

import com.niksob.authorization_service.service.auth.conformation.UserConformationService;
import com.niksob.authorization_service.service.auth.handler.AuthExceptionHandler;
import com.niksob.authorization_service.service.auth.validation.signup.SignupDetailsValidationService;
import com.niksob.authorization_service.service.user.UserService;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SignupServiceImpl implements SignupService {
    private final UserService userService;
    private final UserConformationService userConformationService;
    private final SignupDetailsValidationService signupDetailsValidationService;

    private final AuthExceptionHandler exceptionHandler;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(SignupServiceImpl.class);

    @Override
    public Mono<Void> signup(SignupDetails signupDetails) {
        return Mono.just(signupDetails)
                .filter(signupDetails1 -> signupDetailsValidationService.validate(signupDetails.getEmail()))
                .switchIfEmpty(exceptionHandler.createIncorrectEmailMonoError(signupDetails))

                .filter(signupDetails1 -> signupDetailsValidationService.validate(signupDetails.getRowPassword()))
                .switchIfEmpty(exceptionHandler.createIncorrectPasswordMonoError(signupDetails))

                .flatMap(details -> Mono.zip(
                        userService.existsByEmailOrThrow(signupDetails.getEmail()),
                        checkUsernameExistence(signupDetails),
                        (emailExists, usernameExists) -> emailExists && usernameExists
                ))

                .flatMap(exists -> userConformationService.sendActiveCodeMessage(signupDetails))

                .doOnSuccess(ignore -> log.info("Successful preparing to signup", null, signupDetails))
                .onErrorResume(throwable -> exceptionHandler.createSignupError(throwable, signupDetails));
    }

    @Override
    public Mono<Void> signupByActiveCode(ActiveCode activeCode) {
        return Mono.just(activeCode)
                .map(userConformationService::getUserInfo)
                .flatMap(userService::save)
                .doOnSuccess(userInfo -> log.info("Successful signup of user", null, userInfo))
                .then()
                .onErrorResume(throwable -> exceptionHandler.createSignupError(throwable, activeCode));
    }

    private Mono<Boolean> checkUsernameExistence(SignupDetails signupDetails) {
        if (signupDetails.getUsername() == null || signupDetails.getUsername().getValue() == null) {
            return Mono.just(false);
        }
        return userService.existsByUsernameorThrow(signupDetails.getUsername());
    }
}
