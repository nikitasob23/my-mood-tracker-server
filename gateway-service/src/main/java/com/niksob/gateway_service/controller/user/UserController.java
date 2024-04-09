package com.niksob.gateway_service.controller.user;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.gateway_service.path.controller.user.UserControllerPaths;
import com.niksob.gateway_service.service.auth.UserControllerService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(UserControllerPaths.BASE_URI)
public class UserController {
    private final UserControllerService userControllerService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Mono<UserInfoDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.loadAllByUsername(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .doOnError(user -> log.error("Failure user loading", null, usernameDto));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserInfoDto> save(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.save(userInfoDto)
                .doOnNext(u -> log.debug("Successful user saving", u.getUsername()))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .doOnError(user -> log.error("Failure user saving", null, userInfoDto));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.update(userInfoDto)
                .doOnSuccess(ignore -> log.debug("Successful user updating", userInfoDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(user -> log.error("Failure user updating", null, userInfoDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.delete(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user deletion", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(user -> log.error("Failure user deletion", null, usernameDto));
    }
}
