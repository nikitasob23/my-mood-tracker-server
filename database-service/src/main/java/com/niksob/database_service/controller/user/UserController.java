package com.niksob.database_service.controller.user;

import com.niksob.domain.dto.user.*;
import com.niksob.domain.http.controller.handler.mood.entry.ResourceControllerErrorUtil;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserControllerPaths.BASE_URI)
public class UserController {
    private final UserControllerService userControllerService;

    @Qualifier("userControllerUtil")
    private final ResourceControllerErrorUtil controllerErrorUtil;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserController.class);

    @PostMapping(UserControllerPaths.EMAIL)
    public Mono<Boolean> existsByEmail(@RequestBody EmailDto email) {
        return userControllerService.existsByEmail(email)
                .doOnSuccess(ignore -> log.debug("Successful check user existence by email", email))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerErrorUtil::createLoadingErrorMono);
    }

    @GetMapping
    public Mono<SecurityUserDetailsDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.loadByUsername(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerErrorUtil::createLoadingErrorMono);
    }

    @GetMapping(UserControllerPaths.FULL_USER)
    public Mono<FullUserInfoDto> loadFull(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.loadFullByUsername(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful full user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerErrorUtil::createLoadingErrorMono);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserInfoDto> save(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.save(userInfoDto)
                .doOnNext(u -> log.debug("Successful user saving", u.getUsername()))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(controllerErrorUtil::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.update(userInfoDto)
                .doOnSuccess(ignore -> log.debug("Successful user updating", userInfoDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerErrorUtil::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.delete(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user deletion", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerErrorUtil::createDeleteError);
    }
}
