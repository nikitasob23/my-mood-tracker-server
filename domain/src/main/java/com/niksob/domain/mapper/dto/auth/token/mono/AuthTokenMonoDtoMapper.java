package com.niksob.domain.mapper.dto.auth.token.mono;

import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.model.auth.token.AuthToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthTokenMonoDtoMapper {
    private final AuthTokenDtoMapper dtoMapper;

    public Mono<AuthTokenDto> toDto(Mono<AuthToken> mono) {
        return mono.map(dtoMapper::toDto);
    }

    public Mono<AuthToken> fromDto(Mono<AuthTokenDto> mono) {
        return mono.map(dtoMapper::fromDto);
    }
}
