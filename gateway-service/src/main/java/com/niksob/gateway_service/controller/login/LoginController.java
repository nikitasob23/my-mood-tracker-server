package com.niksob.gateway_service.controller.login;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.gateway_service.path.controller.signup.LoginControllerPaths;
import com.niksob.gateway_service.service.auth.login.LoginControllerService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(LoginControllerPaths.BASE_URI)
public class LoginController {
    private final LoginControllerService loginControllerService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetails) {
        return loginControllerService.signup(signupDetails)
                .doOnSuccess(u -> log.debug("User is signup", signupDetails.getUsername()))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, signupDetails));
    }

    @GetMapping(LoginControllerPaths.SIGNOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOut(@ModelAttribute SignOutDetailsDto signOutDetails) {
        return loginControllerService.signOut(signOutDetails)
                .doOnSuccess(u -> log.debug("User is sign out", signOutDetails))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, signOutDetails));
    }

    @GetMapping(LoginControllerPaths.SIGNOUT_ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOutAll(@RequestParam("user_id") UserIdDto userId) {
        return loginControllerService.signOutAll(userId)
                .doOnSuccess(u -> log.debug("User is sign out from all devices", userId))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, userId));
    }
}
