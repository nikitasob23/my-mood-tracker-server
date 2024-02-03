package com.niksob.database_service.controller.user;

import com.niksob.database_service.exception.resource.ResourceAlreadyExistsException;
import com.niksob.database_service.exception.resource.ResourceDeletionException;
import com.niksob.database_service.exception.resource.ResourceSavingException;
import com.niksob.database_service.exception.resource.ResourceUpdatingException;
import com.niksob.database_service.exception.resource.ResourceNotFoundException;
import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserControllerPaths.BASE_URI)
public class UserController {
    private final UserControllerService userControllerService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Mono<UserInfoDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.load(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(throwable -> createLoadingError(throwable, usernameDto));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserInfoDto> save(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.save(userInfoDto)
                .doOnNext(u -> log.debug("Successful user saving", u.getUsername()))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(throwable -> createSavingError(throwable, userInfoDto));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.update(userInfoDto)
                .doOnSuccess(ignore -> log.debug("Successful user updating", userInfoDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(throwable -> createUpdatingError(throwable, userInfoDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.delete(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user deletion", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(throwable -> createDeleteError(throwable, usernameDto));
    }

    private Mono<UserInfoDto> createLoadingError(Throwable throwable, Object state) {
        log.error("User load error", throwable, state);
        ControllerResponseException errorResponse;
        if (throwable instanceof IllegalUserAccessException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.FORBIDDEN,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            );
        } else if (throwable instanceof ResourceNotFoundException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.NOT_FOUND,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            );
        } else {
            log.error("Controller returning failed status", null, HttpStatus.INTERNAL_SERVER_ERROR);
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }

    private Mono<UserInfoDto> createSavingError(Throwable throwable, Object state) {
        log.error("User save error", throwable, state);
        ControllerResponseException errorResponse;
        if (throwable instanceof ResourceSavingException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            );
        } else if (throwable instanceof ResourceAlreadyExistsException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.CONFLICT,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            );
        } else {
            log.error("Controller returning failed status", throwable, HttpStatus.INTERNAL_SERVER_ERROR);
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }

    private Mono<Void> createUpdatingError(Throwable throwable, Object userInfoDto) {
        log.error("User update error", throwable, userInfoDto);
        if (throwable instanceof ResourceUpdatingException) {
            var errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingError(throwable, userInfoDto).then();
    }

    private Mono<Void> createDeleteError(Throwable throwable, Object usernameDto) {
        log.error("Failed to delete user", throwable, usernameDto);
        if (throwable instanceof ResourceDeletionException) {
            final ControllerResponseException errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingError(throwable, usernameDto).then();
    }
}
