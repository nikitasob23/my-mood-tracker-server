package com.niksob.gateway_service.controller.login;

import com.niksob.gateway_service.path.controller.signup.LoginControllerPaths;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(LoginControllerPaths.BASE_URI)
public class LoginController {
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> signup() {
        return Mono.empty();
    }
}
