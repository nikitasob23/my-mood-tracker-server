package com.niksob.database_service.util.token.auth.filter;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;

import java.util.Set;

public class AuthTokenEntityFilter {
    public static AuthTokenEntity filterByDevice(Set<AuthTokenEntity> tokens, String device) {
        return tokens.stream()
                .filter(saved -> saved.getDevice().equals(device))
                .findFirst().orElse(null);
    }
}
