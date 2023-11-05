package com.niksob.database_service.controller.user;

import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.path.controller.database_service.signup.UserControllerPaths;
import com.niksob.database_service.service.user.UserService;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.user.UsernameDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserControllerPaths.BASE_URI)
public class UserController {

    private final UserService userService;

    private final UsernameDtoMapper usernameDtoMapper;

    private final UserInfoDtoMapper userInfoDtoMapper;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @GetMapping
    public Mono<UserInfoDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return Mono.just(usernameDto)
                .map(usernameDtoMapper::fromDto)
                .map(userService::load)
                .map(userInfoDtoMapper::toDto)
                .onErrorResume(this::createLoadingError);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> save(@RequestBody UserInfoDto userInfoDto) {
        return Mono.just(userInfoDto)
                .map(userInfoDtoMapper::fromDto)
                .doOnNext(userService::save)
                .then()
                .onErrorResume(this::createSavingError);
    }

    private Mono<UserInfoDto> createLoadingError(Throwable throwable) {
        if (throwable instanceof IllegalUserAccessException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.FORBIDDEN,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Void> createSavingError(Throwable throwable) {
        if (throwable instanceof IllegalArgumentException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
