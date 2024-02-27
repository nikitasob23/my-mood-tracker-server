package com.niksob.domain.http.connector;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDatabaseDtoConnector {
    private final HttpClient httpClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public Mono<Void> save(UserInfoDto userInfoDto) {
        return httpClient.sendPostRequest(
                "http://localhost:8081/%s/%s".formatted(contextPath, UserControllerPaths.BASE_URI),
                userInfoDto, UserInfoDto.class, Void.class
        );
    }
}
