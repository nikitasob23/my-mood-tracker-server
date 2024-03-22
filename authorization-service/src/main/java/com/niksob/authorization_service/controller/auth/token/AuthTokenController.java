package com.niksob.authorization_service.controller.auth.token;

import com.niksob.authorization_service.controller.auth.ControllerErrorHandler;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.UserAuthTokenDto;
import com.niksob.domain.path.controller.authorization_service.AuthTokenControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthTokenControllerPaths.BASE_URI)
public class AuthTokenController {
    private final AuthTokenControllerService authTokenControllerService;
    private final ControllerErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenController.class);

    @PostMapping(AuthTokenControllerPaths.SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserAuthTokenDto> generate(@RequestBody RowLoginInDetailsDto rowLoginInDetailsDto) {
        return authTokenControllerService.generate(rowLoginInDetailsDto)
                .doOnNext(authTokenDto -> log.info("Auth token was generated", null, authTokenDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createAuthTokenNotGenerated);
    }
}
