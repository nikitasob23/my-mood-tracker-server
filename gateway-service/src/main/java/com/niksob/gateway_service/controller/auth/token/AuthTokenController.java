package com.niksob.gateway_service.controller.auth.token;

import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.gateway_service.path.controller.auth.token.AuthTokenControllerPaths;
import com.niksob.gateway_service.service.auth.token.AuthTokenControllerService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(AuthTokenControllerPaths.BASE_URI)
public class AuthTokenController {
    private final AuthTokenControllerService authTokenControllerService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthTokenDto> generate(@RequestBody RowLoginInDetailsDto rowLoginInDetails) {
        return authTokenControllerService.generate(rowLoginInDetails)
                .doOnNext(token -> log.debug("Auth token was generated", rowLoginInDetails.getUsername()))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .doOnError(throwable -> log.error(
                        "Controller returning failed response", null, rowLoginInDetails.getUsername()));
    }
}
