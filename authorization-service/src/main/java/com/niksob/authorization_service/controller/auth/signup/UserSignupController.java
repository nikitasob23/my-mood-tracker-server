package com.niksob.authorization_service.controller.auth.signup;

import com.niksob.authorization_service.controller.auth.ControllerErrorHandler;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.path.controller.authorization_service.UserSignupControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserSignupControllerPaths.BASE_URI)
public class UserSignupController {
    private final UserSignupControllerService signupService;
    private final ControllerErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserSignupControllerPaths.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetailsDto) {
        return signupService.execute(signupDetailsDto)
                .doOnSuccess(u -> log.debug("User is signup", signupDetailsDto.getUsername()))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createSignupError);
    }
}
