package com.niksob.authorization_service.service.auth.signup;

import com.niksob.authorization_service.exception.auth.signup.DuplicateSignupAttemptException;
import com.niksob.authorization_service.exception.auth.signup.SignupException;
import com.niksob.authorization_service.mapper.auth.login.SignOutDetailsMapper;
import com.niksob.authorization_service.model.login.password.WrongPasswordException;
import com.niksob.domain.mapper.dto.user.UserDtoMapper;
import com.niksob.domain.model.auth.login.UserPasswordPair;
import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.User;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.activation.ActivationUserDetails;
import com.niksob.authorization_service.service.auth.email.EmailValidationService;
import com.niksob.authorization_service.service.user.UserService;
import com.niksob.authorization_service.values.user.DefaultUserInfo;
import com.niksob.domain.exception.user.email.InvalidEmailException;
import com.niksob.domain.http.connector.microservice.mail_sender.MailSenderConnector;
import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.authorization_service.repository.user.activation.TempActivationUserRepo;
import com.niksob.authorization_service.service.auth.signup.activation.code.ActiveCodeService;
import com.niksob.domain.exception.auth.signup.active_code.InvalidActiveCodeException;
import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.authorization_service.service.auth.token.AuthTokenService;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.model.auth.login.SignOutDetails;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserSignupServiceImpl implements UserSignupService {
    private final AuthTokenService authTokenService;
    private final UserService userService;
    private final ActiveCodeService activeCodeService;
    private final PasswordEncoderService passwordEncoderService;
    private final EmailValidationService emailValidationService;

    private final MailSenderConnector mailSenderConnector;

    private final TempActivationUserRepo tempActivationUserRepo;

    private final UserDtoMapper userDetailsDtoMapper;
    private final SignOutDetailsMapper signOutDetailsMapper;


    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserSignupServiceImpl.class);

    @Override
    public Mono<Void> signup(SignupDetails signupDetails) {
        return Mono.just(signupDetails)
                .filter(details -> emailValidationService.validate(details.getEmail()))
                .switchIfEmpty(createIncorrectEmailMonoError(signupDetails))

                .flatMap(details -> Mono.zip(
                        userService.existsByEmailOrThrow(signupDetails.getEmail()),
                        checkUsernameExistence(signupDetails),
                        (emailExists, usernameExists) -> emailExists && usernameExists
                ))

                .map(exists -> createActivationUserDetails(signupDetails))
                .doOnNext(tempActivationUserRepo::save)
                .flatMap(mailSenderConnector::sendActiveCodeMessage)

                .doOnSuccess(ignore -> log.info("Successful preparing to signup", null, signupDetails))
                .onErrorResume(throwable -> createSignupError(throwable, signupDetails));
    }

    @Override
    public Mono<Void> resetPassword(UserPasswordPair userPasswordPair) {
        return userService.loadById(userPasswordPair.getUserId())
                .flatMap(user -> {
                    if (!passwordEncoderService.matches(userPasswordPair.getOldRowPassword(), user.getPassword())) {
                        return createIncorrectOldPasswordMonoError(user);
                    }
                    log.info("Old password is correct. Change password allowed for user", user);
                    final Password newEncodePassword = passwordEncoderService.encode(userPasswordPair.getNewRowPassword());
                    final UserInfo newUser = new UserInfo(user, newEncodePassword);
                    return userService.update(newUser);
                });
    }

    @Override
    public Mono<Void> signupByActiveCode(ActiveCode activeCode) {
        return Mono.just(activeCode)
                .map(this::loadActiveUserOrThrow)
                .map(userDetailsDtoMapper::toUserInfo)

                .flatMap(userService::save)
                .doOnSuccess(userInfo -> log.info("Successful signup of user", null, userInfo))
                .then()
                .onErrorResume(throwable -> createSignupError(throwable, activeCode));
    }

    private User loadActiveUserOrThrow(ActiveCode activeCode) {
        final User userDetails = tempActivationUserRepo.load(activeCode);
        tempActivationUserRepo.remove(activeCode);
        if (userDetails == null) {
            throw new InvalidActiveCodeException("Wrong activation code");
        }
        return userDetails;
    }

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

    private Mono<Boolean> checkUsernameExistence(SignupDetails signupDetails) {
        if (signupDetails.getUsername() == null || signupDetails.getUsername().getValue() == null) {
            return Mono.just(false);
        }
        return userService.existsByUsernameorThrow(signupDetails.getUsername());
    }

    private ActivationUserDetails createActivationUserDetails(SignupDetails signupDetails) {
        final User userDetails = new User(
                signupDetails.getEmail(),
                DefaultUserInfo.createUsernameIfEmpty(signupDetails),
                passwordEncoderService.encode(signupDetails.getRowPassword()));
        return new ActivationUserDetails(activeCodeService.generate(userDetails.getEmail()), userDetails);
    }

    private Mono<SignupDetails> createIncorrectEmailMonoError(Object state) {
        final String message = "Incorrect email";
        var e = new InvalidEmailException(message);
        log.error(message, e, state);
        return Mono.error(e);
    }

    private <T> Mono<T> createIncorrectOldPasswordMonoError(Object state) {
        final String message = "Incorrect old password for user";
        var e = new WrongPasswordException(message);
        log.error(message, e, state);
        return Mono.error(e);
    }

    private <T> Mono<T> createSignupError(Throwable throwable, Object state) {
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
