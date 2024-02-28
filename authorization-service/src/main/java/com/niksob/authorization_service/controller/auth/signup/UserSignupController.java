package com.niksob.authorization_service.controller.auth.signup;

import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.path.controller.authorization_service.UserSignupControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserSignupControllerPaths.BASE_URI)
public class UserSignupController {
    private final UserSignupControllerService signupService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserSignupControllerPaths.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody SignupDetailsDto signupDetailsDto) {
        return signupService.execute(signupDetailsDto)
                .doOnSuccess(u -> log.debug("User is signup", signupDetailsDto.getUsername()))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(this::createSignupError);
    }

    public <T> Mono<T> createSignupError(Throwable throwable) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerError(throwable);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.CONFLICT) == 0) {
            HttpClientException errorResponse = new HttpClientException(
                    httpClientException.getMessage(),
                    httpClientException,
                    httpClientException.getTimestamp(),
                    httpClientException.getHttpStatus(),
                    "%s/%s".formatted(contextPath, UserSignupControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", throwable, errorResponse);
            return Mono.error(errorResponse);
        }
        return createInternalServerError(throwable);
    }

    private <T> Mono<T> createInternalServerError(Throwable throwable) {
        log.error("Controller returning failed status", throwable, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
