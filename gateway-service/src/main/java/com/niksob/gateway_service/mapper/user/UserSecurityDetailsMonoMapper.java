package com.niksob.gateway_service.mapper.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserSecurityDetailsMonoMapper {
    private final UserSecurityDetailsMapper userSecurityDetailsMapper;

    public Mono<UserSecurityDetails> toMonoUserDetails(Mono<UserInfo> mono) {
        return mono.map(userSecurityDetailsMapper::toUserDetails);
    }
}
