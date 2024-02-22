package com.niksob.database_service.controller.user;

import com.niksob.database_service.util.controller.ResourceControllerUtil;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
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
    private final ResourceControllerUtil controllerUtil;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Mono<UserInfoDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.load(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerUtil::createLoadingErrorMono);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserInfoDto> save(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.save(userInfoDto)
                .doOnNext(u -> log.debug("Successful user saving", u.getUsername()))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(controllerUtil::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody UserInfoDto userInfoDto) {
        return userControllerService.update(userInfoDto)
                .doOnSuccess(ignore -> log.debug("Successful user updating", userInfoDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.delete(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user deletion", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createDeleteError);
    }
}
