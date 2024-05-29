package com.niksob.gateway_service.controller.user;

import com.niksob.domain.dto.user.FullUserInfoDto;
import com.niksob.domain.dto.user.UserDto;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.gateway_service.mapper.user.secure.UserWithoutSecurityDetailsDtoMapper;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import com.niksob.gateway_service.model.user.security.UserWithoutSecurityDetailsDto;
import com.niksob.gateway_service.path.controller.user.UserControllerPaths;
import com.niksob.gateway_service.service.auth.UserControllerService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(UserControllerPaths.BASE_URI)
public class UserController {
    private final UserControllerService userControllerService;
    private final UserWithoutSecurityDetailsDtoMapper userWithoutSecurityDetailsDtoMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Mono<UserDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.loadByUsername(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .doOnError(user -> log.error("Failure user loading", null, usernameDto));
    }

    @GetMapping(UserControllerPaths.FULL_USER)
    public Mono<FullUserInfoDto> loadFull(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.loadFullByUsername(usernameDto)
                .doOnSuccess(ignore -> log.debug("Successful full user loading", usernameDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .doOnError(user -> log.error("Failure full user loading", null, usernameDto));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(
            @RequestBody UserWithoutSecurityDetailsDto userWithoutSecurityDetails,
            @AuthenticationPrincipal UserSecurityDetails userDetails
    ) {
        final UserInfoDto userInfo =
                userWithoutSecurityDetailsDtoMapper.toUserInfo(userWithoutSecurityDetails, userDetails);
        return userControllerService.update(userInfo)
                .doOnSuccess(ignore -> log.debug("Successful user updating", userWithoutSecurityDetails))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .doOnError(user -> log.error("Failure user updating", null, userWithoutSecurityDetails));
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
