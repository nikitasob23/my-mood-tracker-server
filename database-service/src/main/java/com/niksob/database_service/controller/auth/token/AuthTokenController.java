package com.niksob.database_service.controller.auth.token;

import com.niksob.domain.dto.auth.token.UserAuthTokenDto;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.path.controller.database_service.auth.token.AuthTokenDBControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AuthTokenDBControllerPaths.BASE_URI)
@RequiredArgsConstructor
public class AuthTokenController {
    private final AuthTokenControllerService authTokenService;

    @Value("${server.servlet.context-path}")
    private String staticPath;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserAuthTokenDto> save(@RequestBody UserAuthTokenDto authToken) {
        return authTokenService.save(authToken)
                .doOnNext(token -> log.debug("Successful user saving", token))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(this::createSavingError);
    }

    private <T> Mono<T> createSavingError(Throwable throwable) {
        final HttpStatus errorStatus;
        final Throwable errorResponse;
        if (throwable instanceof ResourceSavingException) {
            errorStatus = HttpStatus.BAD_REQUEST;
            errorResponse = new HttpClientException(throwable, errorStatus, staticPath);
        } else {
            errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = new ResponseStatusException(errorStatus);
        }
        log.error("Controller returning failed response", null, errorStatus);
        return Mono.error(errorResponse);
    }
}
