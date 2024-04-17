package com.niksob.database_service.controller.auth.token;

import com.niksob.domain.http.controller.handler.mood.entry.ResourceControllerErrorUtil;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.path.controller.database_service.auth.token.AuthTokenDBControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AuthTokenDBControllerPaths.BASE_URI)
@RequiredArgsConstructor
public class AuthTokenController {
    private final AuthTokenControllerService authTokenService;

    @Qualifier("authTokenControllerUtil")
    private final ResourceControllerErrorUtil controllerErrorUtil;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenController.class);

    @GetMapping(AuthTokenDBControllerPaths.EXISTS)
    public Mono<Boolean> existsByDetails(@ModelAttribute AuthTokenDetailsDto authTokenDetails) {
        return authTokenService.existsByDetails(authTokenDetails)
                .doOnSuccess(ignore -> log.debug("Successful auth token existence check", authTokenDetails))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerErrorUtil::createLoadingErrorMono);
    }

    @GetMapping
    public Mono<EncodedAuthTokenDto> load(@ModelAttribute AuthTokenDetailsDto authTokenDetails) {
        return authTokenService.load(authTokenDetails)
                .doOnSuccess(ignore -> log.debug("Successful auth token loading", authTokenDetails))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerErrorUtil::createLoadingErrorMono);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EncodedAuthTokenDto> save(@RequestBody EncodedAuthTokenDto authToken) {
        return authTokenService.save(authToken)
                .doOnNext(token -> log.debug("Successful auth token saving", token))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(controllerErrorUtil::createSavingError);
    }

    @PutMapping
    public Mono<EncodedAuthTokenDto> update(@RequestBody EncodedAuthTokenDto authTokenDto) {
        return authTokenService.update(authTokenDto)
                .doOnNext(ignore -> log.debug("Successful auth token updating", authTokenDto))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerErrorUtil::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@ModelAttribute AuthTokenDetailsDto authTokenDetails) {
        return authTokenService.delete(authTokenDetails)
                .doOnSuccess(ignore -> log.debug("Successful auth token deletion", authTokenDetails))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerErrorUtil::createDeleteError);
    }

    @DeleteMapping(AuthTokenDBControllerPaths.ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteByUserId(@RequestParam("userId") UserIdDto userId) {
        return authTokenService.deleteByUserId(userId)
                .doOnSuccess(ignore -> log.debug("Successful deletion of all user's auth tokens", userId))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerErrorUtil::createDeleteError);
    }
}
