package com.niksob.authorization_service.controller.auth.token;

import com.niksob.authorization_service.controller.auth.exception.handler.ControllerErrorHandler;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.access.AccessTokenDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.refresh.RefreshTokenDto;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthTokenDto> generate(@RequestBody RowLoginInDetailsDto rowLoginInDetailsDto) {
        return authTokenControllerService.generate(rowLoginInDetailsDto)
                .doOnNext(authTokenDto -> log.info("Auth token was generated", null, authTokenDto))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createAuthTokenException);
    }

    @PostMapping(AuthTokenControllerPaths.REFRESH)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthTokenDto> generateByRefresh(@RequestBody RefreshTokenDto refreshToken) {
        return authTokenControllerService.generateByRefresh(refreshToken)
                .doOnNext(authTokenDto -> log.info("Auth token was generated", null, refreshToken))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(errorHandler::createAuthTokenException);
    }

    @PostMapping(AuthTokenControllerPaths.ACCESS_VALIDATION)
    public Mono<Boolean> validateAccessToken(@RequestBody AccessTokenDto accessToken) {
        return authTokenControllerService.validateAccessToken(accessToken)
                .doOnNext(b -> log.info("Success access token validation", null, b))
                .doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.OK))
                .onErrorResume(errorHandler::createAuthTokenException);
    }

    @PostMapping(AuthTokenControllerPaths.DETAILS)
    public Mono<AuthTokenDetailsDto> extractAuthDetails(@RequestBody AccessTokenDto accessToken) {
        return authTokenControllerService.extractAuthDetails(accessToken)
                .doOnNext(authTokenDetails ->
                        log.info("Success auth token details extraction", null, authTokenDetails)
                ).doOnSuccess(ignore -> log.info("Controller returning success status", HttpStatus.OK))
                .onErrorResume(errorHandler::createAuthTokenException);
    }
}
