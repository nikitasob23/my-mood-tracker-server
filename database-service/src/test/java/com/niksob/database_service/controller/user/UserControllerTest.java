package com.niksob.database_service.controller.user;

import com.niksob.database_service.MainContextTest;
import com.niksob.database_service.config.user.UserTestConfig;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.model.rest.response.error.ErrorDetails;
import com.niksob.domain.path.controller.database_service.signup.UserControllerPaths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {UserTestConfig.class})
public class UserControllerTest extends MainContextTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UsernameDto usernameDto;
    @Autowired
    private UserInfoDto userInfoDto;

    @Test
    public void testLoading() {
        final String paramUri = String.format("/%s?username=%s", UserControllerPaths.BASE_URI, usernameDto.getValue());

        final UserInfoDto resUserInfoDto = webTestClient.get()
                .uri(paramUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserInfoDto.class)
                .getResponseBody()
                .blockLast();
        assertThat(resUserInfoDto).isEqualTo(userInfoDto);
    }

    @Test
    public void testSaving() {
        webTestClient.post()
                .uri(String.format("/%s", UserControllerPaths.BASE_URI))
                .body(Mono.just(userInfoDto), UserInfoDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void testThrowSaving() {
        final UserInfoDto wrongUserInfoDto = new UserInfoDto(
                userInfoDto.getUsername(),
                userInfoDto.getNickname(),
                "0000"
        );

        final ErrorDetails errorDetails = webTestClient.post()
                .uri(String.format("/%s", UserControllerPaths.BASE_URI))
                .body(Mono.just(wrongUserInfoDto), UserInfoDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ErrorDetails.class)
                .getResponseBody()
                .blockLast();

        assertThat(errorDetails.getMessage()).isEqualTo("Unsecure password");
    }
}
