package com.niksob.gateway_service.model.token.authentication;

import com.niksob.domain.model.auth.token.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationToken {
    private final AccessToken accessToken;
    private final String path;
}
