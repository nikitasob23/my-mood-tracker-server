package com.niksob.gateway_service.controller.auth.login;

import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.path.controller.gateway_service.AuthControllerPaths;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import com.niksob.gateway_service.service.auth.login.LoginControllerService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(AuthControllerPaths.BASE_URI)
public class AuthController {
    private final LoginControllerService loginControllerService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthController.class);

    @PostMapping(AuthControllerPaths.SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetails) {
        return loginControllerService.signup(signupDetails)
                .doOnSuccess(u -> log.info("User is signup", signupDetails.getEmail()))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, signupDetails));
    }

    @GetMapping(AuthControllerPaths.ACTIVE_CODE + "/{code}")
    public Mono<Void> signupByActiveCode(@PathVariable String code) {
        return loginControllerService.signupByActiveCode(code)
                .doOnSuccess(u -> log.info("User is signup by active code"))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, code));
    }

    @GetMapping(AuthControllerPaths.SIGNOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOut(@RequestParam("device") String device, @AuthenticationPrincipal UserSecurityDetails userDetails) {
        final SignOutDetailsDto signOutDetails = new SignOutDetailsDto(userDetails.getId().toString(), device);
        return loginControllerService.signOut(signOutDetails)
                .doOnSuccess(u -> log.info("User is sign out", signOutDetails))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, signOutDetails));
    }

    @GetMapping(AuthControllerPaths.SIGNOUT_ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signOutAll(@AuthenticationPrincipal UserSecurityDetails userDetails) {
        final UserIdDto userId = new UserIdDto(userDetails.getId().toString());
        return loginControllerService.signOutAll(userId)
                .doOnSuccess(u -> log.info("User is sign out from all devices", userId))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(throwable ->
                        log.error("Controller returning failed response", null, userId));
    }
}
