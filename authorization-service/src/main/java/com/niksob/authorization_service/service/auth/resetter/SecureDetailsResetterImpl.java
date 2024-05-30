package com.niksob.authorization_service.service.auth.resetter;

import com.niksob.authorization_service.service.auth.conformation.UserConformationService;
import com.niksob.authorization_service.service.auth.handler.AuthExceptionHandler;
import com.niksob.authorization_service.service.auth.validation.signup.SignupDetailsValidationService;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.authorization_service.service.user.UserService;
import com.niksob.domain.model.auth.login.UserEmail;
import com.niksob.domain.model.auth.login.UserPasswordPair;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SecureDetailsResetterImpl implements SecureDetailsResetter {
    private final UserService userService;

    private final UserConformationService userConformationService;
    private final PasswordEncoderService passwordEncoderService;
    private final SignupDetailsValidationService signupDetailsValidationService;

    private final AuthExceptionHandler exceptionHandler;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(SecureDetailsResetterImpl.class);

    @Override
    public Mono<Void> resetPassword(UserPasswordPair userPasswordPair) {
        return userService.loadById(userPasswordPair.getUserId())
                .flatMap(user -> {
                    if (!passwordEncoderService.matches(userPasswordPair.getOldRowPassword(), user.getPassword())) {
                        return exceptionHandler.createIncorrectPasswordMonoError(user);
                    }
                    if (!signupDetailsValidationService.validate(userPasswordPair.getNewRowPassword())) {
                        return exceptionHandler.createNotValidPasswordMonoError(user);
                    }
                    log.info("Old password is correct. Change password allowed for user", user);
                    var newEncodedPassword = passwordEncoderService.encode(userPasswordPair.getNewRowPassword());
                    final UserInfo newUser = new UserInfo(user, newEncodedPassword);
                    return userService.update(newUser);
                });
    }

    @Override
    public Mono<Void> resetEmail(UserEmail userEmail) {
        if (!signupDetailsValidationService.validate(userEmail.getEmail())) {
            return exceptionHandler.createIncorrectEmailMonoError(userEmail);
        }
        return userService.loadById(userEmail.getUserId())
                .map(userInfo -> new UserInfo(userInfo, userEmail.getEmail()))
                .flatMap(userConformationService::sendEmailResettingActiveCodeMessage)
                .doOnSuccess(ignore -> log.info("Successful preparing to reset email", null, userEmail))
                .onErrorResume(throwable -> exceptionHandler.createSignupError(throwable, userEmail));
    }

    @Override
    public Mono<Void> resetEmailByActiveCode(ActiveCode activeCode) {
        return Mono.just(activeCode)
                .map(userConformationService::getUserInfo)
                .flatMap(userService::update)
                .doOnSuccess(userInfo -> log.info("Successful resetting of email"))
                .onErrorResume(throwable -> exceptionHandler.createResettingEmailMonoError(throwable, activeCode));
    }
}
