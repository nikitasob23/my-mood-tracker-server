package com.niksob.domain.mapper.dto.auth.token.encoded.mono;

import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.encoded.EncodedAuthTokenDtoMapper;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class EncodedAuthTokenMonoDtoMapper {
    private final EncodedAuthTokenDtoMapper dtoMapper;

    public Mono<EncodedAuthToken> fromDto(Mono<EncodedAuthTokenDto> mono) {
        return mono.map(dtoMapper::fromDto);
    }

    public Mono<EncodedAuthTokenDto> toDto(Mono<EncodedAuthToken> mono) {
        return mono.map(dtoMapper::toDto);
    }
}
