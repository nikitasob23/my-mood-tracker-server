package com.niksob.gateway_service.service.security;

import com.niksob.domain.http.connector.UserDatabaseConnector;
import com.niksob.domain.mapper.user.UsernameMapper;
import com.niksob.gateway_service.mapper.user.UserSecurityDetailsMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserSecurityDetailsService implements UserDetailsService {
    private final UserDatabaseConnector userDatabaseConnector;

    private final UserSecurityDetailsMapper userSecurityDetailsMapper;
    private final UsernameMapper usernameMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Mono.fromCallable(() -> usernameMapper.toUsername(username))
                .flatMap(userDatabaseConnector::load)
                .map(userSecurityDetailsMapper::toUserDetails)
                .block();
    }
}
