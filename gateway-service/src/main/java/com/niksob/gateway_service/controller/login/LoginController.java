package com.niksob.gateway_service.controller.login;

import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.gateway_service.path.controller.signup.LoginControllerPaths;
import com.niksob.gateway_service.service.auth.login.LoginControllerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(LoginControllerPaths.BASE_URI)
public class LoginController {
    private final LoginControllerService loginControllerService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetails) {
        return loginControllerService.signup(signupDetails);
    }
}
