package com.niksob.domain.http.connector;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.domain.path.microservice.MicroservicePath;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDatabaseDtoConnector {
    private final HttpClient httpClient;

    private final RestPath restPath;

    public Mono<UserInfoDto> load(UsernameDto usernameDto) {
        return httpClient.sendPostRequest(
                restPath.get(UserControllerPaths.BASE_URI),
                usernameDto, UsernameDto.class, UserInfoDto.class
        );
    }

    public Mono<Void> save(UserInfoDto userInfoDto) {
        return httpClient.sendPostRequest(
                restPath.get(UserControllerPaths.BASE_URI),
                userInfoDto, UserInfoDto.class, Void.class
        );
    }
}
