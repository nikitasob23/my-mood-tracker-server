package com.niksob.authorization_service.controller.auth.login;

import com.niksob.authorization_service.controller.auth.exception.handler.ControllerErrorHandler;
import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.path.controller.authorization_service.LoginControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginControllerService loginControllerService;
    private final ControllerErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginControllerPaths.class);

    @PostMapping(LoginControllerPaths.SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetailsDto) {
        return loginControllerService.signup(signupDetailsDto)
                .doOnSuccess(u -> log.info("User is prepare to signup", signupDetailsDto.getEmail()))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(LoginControllerPaths.ACTIVE_CODE)
    public Mono<Void> signupByActiveCode(@RequestParam("active_code")ActiveCodeDto activeCode) {
        return loginControllerService.signupByActiveCode(activeCode)
                .doOnSuccess(u -> log.info("User is signup by active code"))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(LoginControllerPaths.SIGNOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOut(@ModelAttribute SignOutDetailsDto signOutDetails) {
        return loginControllerService.signOut(signOutDetails)
                .doOnSuccess(u -> log.info("User is sign out", signOutDetails))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(LoginControllerPaths.SIGNOUT_ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOutAll(@RequestParam("user_id") UserIdDto userId) {
        return loginControllerService.signOutAll(userId)
                .doOnSuccess(ignore -> log.info("User is sign out from all devices", userId))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(errorHandler::createLoginError);
    }
}
