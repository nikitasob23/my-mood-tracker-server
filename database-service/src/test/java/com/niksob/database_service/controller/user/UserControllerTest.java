package com.niksob.database_service.controller.user;

import com.niksob.database_service.MainContextTest;
import com.niksob.database_service.config.user.UserEntityTestConfig;
import com.niksob.database_service.config.user.UserTestConfig;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {UserTestConfig.class, UserEntityTestConfig.class})
public class UserControllerTest extends MainContextTest {
    @MockBean
    private static UserRepository userRepository;
    private boolean userRepoMocked = false;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UsernameDto usernameDto;
    @Autowired
    private UserInfoDto userInfoDto;
    @Autowired
    private UserEntity userEntity;

    @BeforeEach
    public void mockUserRepo() {
        if (userRepoMocked) {
            return;
        }
        Mockito.when(userRepository.getByUsername(userEntity.getUsername())).thenReturn(userEntity);
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        userRepoMocked = true;
    }

    @Test
    public void testLoadingExistsUserByUsername() {
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
    public void testSavingNewUser() {
        webTestClient.post()
                .uri(String.format("/%s", UserControllerPaths.BASE_URI))
                .body(Mono.just(userInfoDto), UserInfoDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void testUpdatingUser() {
        webTestClient.put()
                .uri(String.format("/%s", UserControllerPaths.BASE_URI))
                .body(Mono.just(userInfoDto), UserInfoDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void testDeletingExistsUserByUsername() {
        final String paramUri = String.format("/%s?username=%s", UserControllerPaths.BASE_URI, usernameDto.getValue());

        webTestClient.delete()
                .uri(paramUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }
}
