package com.niksob.authorization_service.controller.auth.login;

import com.niksob.authorization_service.controller.auth.exception.handler.ControllerErrorHandler;
import com.niksob.domain.dto.auth.login.UserPasswordPairDto;
import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.path.controller.authorization_service.AuthControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final LoginControllerService loginControllerService;
    private final ControllerErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthControllerPaths.class);

    @PostMapping(AuthControllerPaths.SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetailsDto) {
        return loginControllerService.signup(signupDetailsDto)
                .doOnSuccess(u -> log.info("User is prepare to signup", signupDetailsDto.getEmail()))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createLoginError);
    }

    @PostMapping(AuthControllerPaths.PASSWORD_RESETTING)
    public Mono<Void> resetPassword(@RequestBody UserPasswordPairDto passwordPair) {
        return loginControllerService.resetPassword(passwordPair)
                .doOnSuccess(u ->
                        log.info("Success password resetting for user with id", passwordPair.getUserId())
                ).doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.OK))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(AuthControllerPaths.ACTIVE_CODE)
    public Mono<Void> signupByActiveCode(@RequestParam("active_code") ActiveCodeDto activeCode) {
        return loginControllerService.signupByActiveCode(activeCode)
                .doOnSuccess(u -> log.info("User is signup by active code"))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(AuthControllerPaths.SIGNOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOut(@ModelAttribute SignOutDetailsDto signOutDetails) {
        return loginControllerService.signOut(signOutDetails)
                .doOnSuccess(u -> log.info("User is sign out", signOutDetails))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(AuthControllerPaths.SIGNOUT_ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOutAll(@RequestParam("userId") UserIdDto userId) {
        return loginControllerService.signOutAll(userId)
                .doOnSuccess(ignore -> log.info("User is sign out from all devices", userId))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(errorHandler::createLoginError);
    }
}
