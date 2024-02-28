package com.niksob.authorization_service.controller.auth.token;

import com.niksob.authorization_service.exception.username.UsernameNotFoundAuthException;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.path.controller.authorization_service.AuthTokenControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthTokenControllerPaths.BASE_URI)
public class AuthTokenController {
    private final AuthTokenControllerService authTokenControllerService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenController.class);

    @PostMapping(AuthTokenControllerPaths.SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthTokenDto> generate(RowLoginInDetailsDto rowLoginInDetailsDto) {
        return authTokenControllerService.generate(rowLoginInDetailsDto)
                .doOnNext(authTokenDto -> log.info("Auth token was generated", null, authTokenDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(this::createAuthTokenNotGenerated);
    }

    private Mono<AuthTokenDto> createAuthTokenNotGenerated(Throwable throwable) {
        if (throwable instanceof UsernameNotFoundAuthException) {
            var errorResponse = new HttpClientException(throwable, HttpStatus.NOT_FOUND, contextPath);
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        log.error("Controller returning failed status", null, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
