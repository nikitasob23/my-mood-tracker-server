package com.niksob.domain.mapper.dto.auth.token.details.mono;

import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDetailsDtoMapper;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthTokenDetailsDtoMonoMapper {
    private final AuthTokenDetailsDtoMapper authTokenDetailsDtoMapper;

    public Mono<AuthTokenDetails> fromDto(Mono<AuthTokenDetailsDto> mono) {
        return mono.map(authTokenDetailsDtoMapper::fromDto);
    }

    public Mono<AuthTokenDetailsDto> toDto(Mono<AuthTokenDetails> mono) {
        return mono.map(authTokenDetailsDtoMapper::toDto);
    }
}
