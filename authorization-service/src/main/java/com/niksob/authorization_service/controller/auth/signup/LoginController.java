package com.niksob.authorization_service.controller.auth.signup;

import com.niksob.authorization_service.controller.auth.ControllerErrorHandler;
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
    private final LoginControllerService signupService;
    private final ControllerErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginControllerPaths.class);

    @PostMapping(LoginControllerPaths.SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetailsDto) {
        return signupService.signup(signupDetailsDto)
                .doOnSuccess(u -> log.debug("User is signup", signupDetailsDto.getUsername()))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createLoginError);
    }

    @GetMapping(LoginControllerPaths.SIGNOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOut(@ModelAttribute SignOutDetailsDto signOutDetails) {
        return signupService.signOut(signOutDetails)
                .doOnSuccess(u -> log.debug("User is sign out", signOutDetails))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(errorHandler::createLoginError);
    }
}
