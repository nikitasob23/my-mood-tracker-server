package com.niksob.authorization_service.mapper.auth.token;

import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.model.auth.token.AuthToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthTokenMonoDtoMapper {
    private final AuthTokenDtoMapper authTokenDtoMapper;

    public Mono<AuthTokenDto> toDtoMono(Mono<AuthToken> mono) {
        return mono.map(authTokenDtoMapper::toDto);
    }
}
