package com.niksob.domain.mapper.dto.auth.token.mono;

import com.niksob.domain.dto.auth.token.UserAuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.model.auth.token.UserAuthToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthTokenMonoDtoMapper {
    private final AuthTokenDtoMapper dtoMapper;

    public Mono<UserAuthTokenDto> toDto(Mono<UserAuthToken> mono) {
        return mono.map(dtoMapper::toDto);
    }

    public Mono<UserAuthToken> fromDto(Mono<UserAuthTokenDto> mono) {
        return mono.map(dtoMapper::fromDto);
    }
}
