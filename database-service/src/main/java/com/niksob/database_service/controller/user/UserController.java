package com.niksob.database_service.controller.user;

import com.niksob.database_service.exception.entity.EntityAlreadyExistsException;
import com.niksob.database_service.exception.entity.EntityNotDeletedException;
import com.niksob.database_service.exception.entity.EntitySavingException;
import com.niksob.database_service.exception.entity.EntityUpdatingException;
import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
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
                .onErrorResume(throwable -> createLoadingError(throwable, usernameDto));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> save(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.save(userInfoDto)
                .then()
                .onErrorResume(throwable -> createSavingError(throwable, userInfoDto));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.update(userInfoDto)
                .then()
                .onErrorResume(throwable -> createUpdatingError(throwable, userInfoDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.delete(usernameDto)
                .then()
                .onErrorResume(throwable -> createDeleteError(throwable, usernameDto));
    }

    private Mono<UserInfoDto> createLoadingError(Throwable throwable, Object state) {
        log.error("User load error", throwable, state);
        if (throwable instanceof IllegalUserAccessException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.FORBIDDEN,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        } else if (throwable instanceof EntityNotFoundException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.NOT_FOUND,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Void> createSavingError(Throwable throwable, Object state) {
        log.error("User save error", throwable, state);
        if (throwable instanceof EntitySavingException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        } else if (throwable instanceof EntityAlreadyExistsException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.CONFLICT,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Void> createUpdatingError(Throwable throwable, Object userInfoDto) {
        log.error("User update error", throwable, userInfoDto);
        if (throwable instanceof EntityUpdatingException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return createLoadingError(throwable, userInfoDto).then();
    }

    private Mono<Void> createDeleteError(Throwable throwable, Object usernameDto) {
        log.error("User delete error", throwable, usernameDto);
        if (throwable instanceof EntityNotDeletedException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return createLoadingError(throwable, usernameDto).then();
    }
}
